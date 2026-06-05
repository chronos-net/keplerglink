'use client';

import { useCallback, useMemo, useState } from 'react';
import { useAuthContext } from '@/context/AuthContext';

export function useLoginForm(opts?: { onSuccess?: () => void }) {
  const { login } = useAuthContext();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const valid = useMemo(
    () => username.trim().length > 0 && password.trim().length > 0,
    [username, password]
  );

  const submit = useCallback(async () => {
    if (!valid) {
      setError('Completa usuario y contraseña');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await login({ username, password });
      opts?.onSuccess?.();
    } catch (e: unknown) {
      // evita `any`: intentamos leer un mensaje razonable
      const msg =
        e instanceof Error ? e.message : 'No fue posible iniciar sesión';
      setError(msg);
    } finally {
      setLoading(false);
    }
  }, [login, opts, password, username, valid]);

  return {
    username,
    password,
    loading,
    error,
    valid,
    setUsername,
    setPassword,
    submit,
  } as const;
}
