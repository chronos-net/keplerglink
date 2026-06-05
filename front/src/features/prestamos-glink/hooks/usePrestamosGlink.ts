'use client';

import { useCallback, useRef, useState } from 'react';
import type { PrestamosGlinkRequest, PrestamosGlinkResponse } from '../types/prestamos-glink.types';
import { postPrestamosGlink } from '../services/prestamos-glink.api';
import { getHttpErrorMessage, isCanceledError } from '@/utils/http-error';

export function usePrestamosGlink() {
  const [data, setData] = useState<PrestamosGlinkResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const abortRef = useRef<AbortController | null>(null);

  const consultar = useCallback(async (payload: PrestamosGlinkRequest) => {
    abortRef.current?.abort();
    abortRef.current = new AbortController();

    setLoading(true);
    setError(null);

    try {
      const res = await postPrestamosGlink(payload, { signal: abortRef.current.signal });
      setData(res);
    } catch (e: unknown) {
      if (isCanceledError(e)) return;
      setError(getHttpErrorMessage(e, 'No se pudo consultar préstamos GLINK.'));
      setData(null);
    } finally {
      setLoading(false);
    }
  }, []);

  const reset = useCallback(() => {
    abortRef.current?.abort();
    setData(null);
    setError(null);
    setLoading(false);
  }, []);

  return { data, loading, error, consultar, reset };
}