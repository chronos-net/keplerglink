'use client';

import React, { createContext, useContext, useEffect, useMemo, useState, useCallback } from 'react';

type SidebarCtx = {
  open: boolean;
  toggle: () => void;
  set: (v: boolean) => void;
};

const STORAGE_KEY = 'ui:sidebar';

const Ctx = createContext<SidebarCtx | null>(null);

export function SidebarProvider({ children }: { children: React.ReactNode }) {
  // Lazy init: server -> false, client -> localStorage
  const [open, setOpen] = useState<boolean>(() => {
    if (typeof window === 'undefined') return false;
    return localStorage.getItem(STORAGE_KEY) === '1';
  });

  const persist = useCallback((v: boolean) => {
    if (typeof window === 'undefined') return;
    localStorage.setItem(STORAGE_KEY, v ? '1' : '0');
  }, []);

  const toggle = useCallback(() => {
    setOpen((prev) => {
      const next = !prev;
      persist(next);
      return next;
    });
  }, [persist]);

  const set = useCallback((v: boolean) => {
    setOpen(v);
    persist(v);
  }, [persist]);

  // Sync entre pestañas
  useEffect(() => {
    if (typeof window === 'undefined') return;

    const onStorage = (e: StorageEvent) => {
      if (e.key !== STORAGE_KEY) return;
      setOpen(e.newValue === '1');
    };

    window.addEventListener('storage', onStorage);
    return () => window.removeEventListener('storage', onStorage);
  }, []);

  const value = useMemo(() => ({ open, toggle, set }), [open, toggle, set]);

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useSidebar() {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error('useSidebar debe usarse dentro de <SidebarProvider>');
  return ctx;
}
