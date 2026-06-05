'use client';

import { useCallback, useRef, useState } from 'react';
import { postValidacionPrestamos } from '../services/prestamos.service';
import type { PrestamosRequest, PrestamosResponse } from '../types/prestamos.types';
import { isCanceledError } from '@/utils/http-error';

export function usePrestamos() {
  const [data, setData] = useState<PrestamosResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const abortRef = useRef<AbortController | null>(null);

  const validar = useCallback(
    async (payload: PrestamosRequest) => {
      if (abortRef.current) {
        abortRef.current.abort();
      }

      const controller = new AbortController();
      abortRef.current = controller;

      setLoading(true);
      setError(null);

      try {
        const res = await postValidacionPrestamos(payload, { signal: controller.signal });
        setData(res);
      } catch (err: unknown) {
        if (isCanceledError(err)) return;
        console.error('PRESTAMOS ERROR', err);
        setError('No se pudieron consultar los préstamos. Intenta nuevamente.');
        setData(null);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const reset = useCallback(() => {
    abortRef.current?.abort();
    setData(null);
    setError(null);
    setLoading(false);
  }, []);

  return {
    data,
    loading,
    error,
    validar,
    reset,
  };
}