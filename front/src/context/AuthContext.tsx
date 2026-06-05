'use client';

import React, {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import { usePathname, useRouter } from 'next/navigation';

import type { User } from '@/features/auth/types/auth.types';
import { login as apiLogin, logout as apiLogout, me as apiMe } from '@/features/auth/auth.service';

type AuthContextValue = {
  user: User | null;
  loading: boolean;

  /** Login: crea cookie en backend + sincroniza /auth/me */
  login: (payload: { username: string; password: string }) => Promise<User>;

  /** Logout: borra cookie en backend (best-effort) + limpia estado */
  logout: () => Promise<void>;

  /** Revalida sesión (útil para refresh manual / after actions) */
  refresh: () => Promise<User | null>;
};

const AuthContext = createContext<AuthContextValue | null>(null);

/** Canal multi-tab */
const BC_NAME = 'auth';

function useAuthBroadcast(onLogout: () => void) {
  const bcRef = useRef<BroadcastChannel | null>(null);
  const hasBC = typeof window !== 'undefined' && 'BroadcastChannel' in window;

  useEffect(() => {
    if (typeof window === 'undefined') return;

    if (hasBC) {
      bcRef.current = new BroadcastChannel(BC_NAME);
      bcRef.current.onmessage = (ev) => {
        if (ev.data === 'logout') onLogout();
      };
      return () => bcRef.current?.close();
    }

    const onStorage = (e: StorageEvent) => {
      if (e.key === 'auth:bc' && e.newValue === 'logout') onLogout();
    };

    window.addEventListener('storage', onStorage);
    return () => window.removeEventListener('storage', onStorage);
  }, [hasBC, onLogout]);

  const broadcastLogout = useCallback(() => {
    if (typeof window === 'undefined') return;

    if (hasBC) bcRef.current?.postMessage('logout');
    else {
      localStorage.setItem('auth:bc', 'logout');
      localStorage.removeItem('auth:bc');
    }
  }, [hasBC]);

  return { broadcastLogout };
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const pathname = usePathname();

  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  /** Evita que el "from" se quede apuntando a /login */
  const safeFrom = useMemo(() => {
    const p = pathname || '/dashboard';
    return p.startsWith('/login') ? '/dashboard' : p;
  }, [pathname]);

  const goLogin = useCallback(
    (reason?: 'expired' | 'logout' | 'multi') => {
      const from = encodeURIComponent(safeFrom);
      const q =
        reason === 'expired'
          ? `?expired=1&from=${from}`
          : reason === 'multi'
            ? `?multi=1&from=${from}`
            : `?loggedOut=1&from=${from}`;

      router.replace(`/login${q}`);
      router.refresh();
    },
    [router, safeFrom]
  );

  const onMultiLogout = useCallback(() => {
    setUser(null);
    goLogin('multi');
  }, [goLogin]);

  const { broadcastLogout } = useAuthBroadcast(onMultiLogout);

  /**
   * ✅ refresh: la ÚNICA fuente de verdad es apiMe()
   * - Si hay cookie válida: regresa User
   * - Si no hay sesión: regresa null
   */
  const refresh = useCallback(async (): Promise<User | null> => {
    try {
      const u = await apiMe(); // <-- ya viene normalizado
      return u;
    } catch {
      return null;
    }
  }, []);

  /** Bootstrap */
  useEffect(() => {
    let alive = true;

    (async () => {
      const u = await refresh();
      if (!alive) return;
      setUser(u);
      setLoading(false);
    })();

    return () => {
      alive = false;
    };
  }, [refresh]);

  /** Expiración global (viene del interceptor) */
  useEffect(() => {
    if (typeof window === 'undefined') return;

    const onExpired = () => {
      setUser(null);
      broadcastLogout();
      goLogin('expired');
    };

    window.addEventListener('auth:expired', onExpired as EventListener);
    return () => window.removeEventListener('auth:expired', onExpired as EventListener);
  }, [broadcastLogout, goLogin]);

  /** Auto logout por inactividad */
  useEffect(() => {
    if (typeof window === 'undefined') return;

    const IDLE_MS = 30 * 60 * 1000;
    let t: number | undefined;

    const arm = () => {
      if (t) window.clearTimeout(t);
      if (!user) return;
      t = window.setTimeout(() => {
        window.dispatchEvent(new Event('auth:expired'));
      }, IDLE_MS);
    };

    const onActivity = () => {
      if (document.visibilityState === 'hidden') return;
      arm();
    };

    const opts: AddEventListenerOptions = { passive: true };
    const events = ['click', 'keydown', 'mousemove', 'scroll', 'touchstart', 'visibilitychange'] as const;

    events.forEach((ev) => window.addEventListener(ev, onActivity, opts));
    arm();

    return () => {
      if (t) window.clearTimeout(t);
      events.forEach((ev) => window.removeEventListener(ev, onActivity));
    };
  }, [user]);

  /** Login (sin redirección: el formulario decide) */
  const login = useCallback(
    async (payload: { username: string; password: string }) => {
      setLoading(true);
      try {
        await apiLogin(payload);

        const u = await refresh();
        setUser(u);

        if (!u) throw new Error('No se pudo validar sesión (user null).');
        return u;
      } finally {
        setLoading(false);
      }
    },
    [refresh]
  );

  const logout = useCallback(async () => {
    try {
      await apiLogout();
    } catch {
      // best-effort
    }
    setUser(null);
    broadcastLogout();
    goLogin('logout');
  }, [broadcastLogout, goLogin]);

  const value = useMemo<AuthContextValue>(
    () => ({ user, loading, login, logout, refresh }),
    [user, loading, login, logout, refresh]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuthContext() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuthContext debe usarse dentro de <AuthProvider>');
  return ctx;
}

/** Gate rutas privadas (/dashboard...) */
export function AuthGate({ children }: { children: React.ReactNode }) {
  const { user, loading } = useAuthContext();
  const router = useRouter();
  const pathname = usePathname();

  const isPrivate = (pathname ?? '').startsWith('/dashboard');

  useEffect(() => {
    if (loading) return;
    if (isPrivate && !user) {
      const from = encodeURIComponent(pathname || '/dashboard');
      router.replace(`/login?from=${from}`);
    }
  }, [isPrivate, loading, pathname, router, user]);

  if (loading) return null;
  if (isPrivate && !user) return null;

  return <>{children}</>;
}
