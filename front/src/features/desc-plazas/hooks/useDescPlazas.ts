'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import axios from 'axios';

import { postDescPlazas } from '../services/desc-plazas.api';
import { DescPlazasRequest, DescPlazasResponseDto } from '../types/desc-plazas.dto';

type State =
  | { status: 'idle'; data: null; error: null }
  | { status: 'loading'; data: DescPlazasResponseDto | null; error: null }
  | { status: 'success'; data: DescPlazasResponseDto; error: null }
  | { status: 'error'; data: DescPlazasResponseDto | null; error: string };

type CacheKey = string;

const makeCacheKey = (p: DescPlazasRequest): CacheKey => {
  const anio = String(p.anio);
  const quincena = String(p.quincena);
  const ney = (p.neyemp ?? '').trim();
  const nom = (p.nombreSp ?? '').trim().toUpperCase();

  if (ney) return `${anio}::${quincena}::NEYEMP:${ney}`;
  return `${anio}::${quincena}::NOMBRE:${nom}`;
};

const validatePayload = (payload: DescPlazasRequest): string | null => {
  if (!payload.anio) return 'Proporciona el año.';
  if (!payload.quincena) return 'Proporciona la quincena.';
  if (!payload.neyemp && !payload.nombreSp) {
    return 'Captura nombre o clave del servidor público.';
  }
  return null;
};

function isAbortLike(err: unknown) {
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
}

export function useDescPlazas() {
  const [state, setState] = useState<State>({
    status: 'idle',
    data: null,
    error: null,
  });

  const abortRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, DescPlazasResponseDto>());

  useEffect(() => {
    return () => abortRef.current?.abort();
  }, []);

  const consultar = useCallback(async (payload: DescPlazasRequest) => {
    const validationError = validatePayload(payload);

    if (validationError) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: validationError,
      }));
      return null;
    }

    const key = makeCacheKey(payload);

    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({
        status: 'success',
        data: hit,
        error: null,
      });
      return hit;
    }

    abortRef.current?.abort();

    const ac = new AbortController();
    abortRef.current = ac;

    setState((prev) => ({
      status: 'loading',
      data: prev.data,
      error: null,
    }));

    try {
      const res = await postDescPlazas(payload, { signal: ac.signal });

      cacheRef.current.set(key, res);

      setState({
        status: 'success',
        data: res,
        error: null,
      });

      return res;
    } catch (e: unknown) {
      if (isAbortLike(e)) return null;

      let message = 'No se pudo consultar los desgloses de plazas.';

      if (axios.isAxiosError(e)) {
        const status = e.response?.status;

        if (status === 404) {
          message =
            'No encontramos desgloses para los datos capturados. Verifica el nombre, año y quincena.';
        } else if (status === 500) {
          message =
            'Ocurrió un problema al consultar los desgloses. Intenta nuevamente más tarde.';
        } else if (!e.response) {
          message =
            'No fue posible conectar con el servidor. Verifica tu conexión e intenta nuevamente.';
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

  const cancel = useCallback(() => {
    abortRef.current?.abort();
  }, []);

  const reset = useCallback(() => {
    abortRef.current?.abort();
    setState({
      status: 'idle',
      data: null,
      error: null,
    });
  }, []);

  const loading = state.status === 'loading';
  const ok = state.status === 'success';
  const idle = state.status === 'idle';

  return useMemo(() => {
    return {
      ...state,
      loading,
      ok,
      idle,
      consultar,
      cancel,
      reset,
      hasData: !!state.data,
      isEmpty: !loading && !state.data && !state.error,
    };
  }, [state, loading, ok, idle, consultar, cancel, reset]);
}