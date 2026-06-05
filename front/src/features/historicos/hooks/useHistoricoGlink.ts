'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import axios from 'axios';
import type { HistoricoRequest, HistoricoResponse } from '../types/historico.types';
import { postHistoricoGlink } from '../services/historico.service';

const isAbortLike = (err: unknown) => {
  if (typeof err === 'object' && err) {
    const e = err as { name?: string; code?: string; message?: string };
    return (
      e.name === 'AbortError' ||
      e.code === 'ERR_CANCELED' ||
      e.name === 'CanceledError' ||
      (e.message ?? '').toLowerCase().includes('canceled')
    );
  }
  return false;
};

type State =
  | { status: 'idle'; error: null; data: null }
  | { status: 'loading'; error: null; data: HistoricoResponse | null }
  | { status: 'success'; error: null; data: HistoricoResponse }
  | { status: 'error'; error: string; data: HistoricoResponse | null };

type CacheKey = string;

const makeKey = (p: HistoricoRequest): CacheKey => {
  const ney = (p.neyemp ?? '').trim();
  return `GLINK::HIST::${ney}`;
};

export function useHistoricoGlink() {
  const [state, setState] = useState<State>({
    status: 'idle',
    error: null,
    data: null,
  });

  const acRef = useRef<AbortController | null>(null);
  const activeKeyRef = useRef<CacheKey | null>(null);
  const cacheRef = useRef(new Map<CacheKey, HistoricoResponse>());

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const validar = useCallback(async (payload: HistoricoRequest) => {
    const neyemp = (payload.neyemp ?? '').trim();

    if (!neyemp) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'Captura la clave del servidor público.',
      }));
      return null;
    }

    const key = makeKey(payload);
    activeKeyRef.current = key;

    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit, error: null });
      return hit;
    }

    acRef.current?.abort();
    const ac = new AbortController();
    acRef.current = ac;

    setState((prev) => ({
      status: 'loading',
      data: prev.data,
      error: null,
    }));

    try {
      const data = await postHistoricoGlink(payload, { signal: ac.signal });

      if (activeKeyRef.current !== key) return null;

      cacheRef.current.set(key, data);
      setState({ status: 'success', data, error: null });
      return data;
    } catch (err: unknown) {
      if (isAbortLike(err)) return null;
      if (activeKeyRef.current !== key) return null;

      let message = 'No se pudo consultar el histórico.';

      if (axios.isAxiosError(err)) {
        const status = err.response?.status;

        if (status === 404) {
          message = 'No encontramos histórico para la clave capturada.';
        } else if (status === 500) {
          message = 'Ocurrió un problema al consultar el histórico. Intenta nuevamente más tarde.';
        } else if (!err.response) {
          message = 'No fue posible conectar con el servidor. Verifica tu conexión e intenta nuevamente.';
        }
      }

      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: message,
      }));

      return null;
    }
  }, []);

  const reset = useCallback(() => {
    acRef.current?.abort();
    activeKeyRef.current = null;
    setState({ status: 'idle', data: null, error: null });
  }, []);

  const loading = state.status === 'loading';
  const ok = state.status === 'success';
  const idle = state.status === 'idle';

  return useMemo(() => {
    return { ...state, loading, ok, idle, validar, reset };
  }, [state, loading, ok, idle, validar, reset]);
}