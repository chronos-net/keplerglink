'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import type { PensionesRequest, PensionesResponse } from '../types/pensiones.types';
import { getPensiones } from '../services/pensiones.service';

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
  | { status: 'idle'; error: null; data: PensionesResponse | null; hasPensiones: boolean | null }
  | { status: 'loading'; error: null; data: PensionesResponse | null; hasPensiones: boolean | null }
  | { status: 'success'; error: null; data: PensionesResponse; hasPensiones: boolean }
  | { status: 'error'; error: string; data: PensionesResponse | null; hasPensiones: boolean | null };

type CacheKey = string;

const makeCacheKey = (p: PensionesRequest): CacheKey => {
  const anio = String(p.anio);
  const periodo = String(p.periodo).trim();
  const ney = (p.neyemp ?? '').trim();
  return `${anio}::${periodo}::NEYEMP:${ney}`;
};

type CacheValue = {
  data: PensionesResponse;
  hasPensiones: boolean;
};

export const usePensiones = () => {
  const [open, setOpen] = useState(false);

  const [state, setState] = useState<State>({
    status: 'idle',
    error: null,
    data: null,
    hasPensiones: null,
  });

  const acRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, CacheValue>());
  const activeKeyRef = useRef<CacheKey | null>(null);

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const run = useCallback(async (payload: PensionesRequest) => {
    if (!payload?.anio || !payload?.periodo || !payload?.neyemp) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'Proporciona anio, periodo y neyemp.',
        hasPensiones: prev.hasPensiones,
      }));
      return null;
    }

    const key = makeCacheKey(payload);
    activeKeyRef.current = key;

    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit.data, error: null, hasPensiones: hit.hasPensiones });
      return hit.data;
    }

    acRef.current?.abort();
    const ac = new AbortController();
    acRef.current = ac;

    setState((prev) => ({
      status: 'loading',
      data: prev.data,
      error: null,
      hasPensiones: prev.hasPensiones,
    }));

    try {
      const data = await getPensiones(payload, { signal: ac.signal });

      if (activeKeyRef.current !== key) return null;

      const cleaned = (data.pension ?? []).filter((x) => (x?.negnom ?? '').trim().length > 0);
      const normalized: PensionesResponse = { ...data, pension: cleaned };

      const has = cleaned.length > 0;

      cacheRef.current.set(key, { data: normalized, hasPensiones: has });
      setState({ status: 'success', data: normalized, error: null, hasPensiones: has });

      return normalized;
    } catch (err: unknown) {
      if (isAbortLike(err)) return null;
      if (activeKeyRef.current !== key) return null;

      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'No se pudieron obtener pensiones.',
        hasPensiones: prev.hasPensiones,
      }));
      return null;
    }
  }, []);

  const prime = useCallback(async (payload: PensionesRequest) => {
    if (!payload?.anio || !payload?.periodo || !payload?.neyemp) {
      activeKeyRef.current = null;
      setState((prev) => ({
        status: prev.status === 'success' ? 'success' : 'idle',
        data: prev.data,
        error: null,
        hasPensiones: null,
      } as State));
      return null;
    }

    const key = makeCacheKey(payload);

    if (activeKeyRef.current !== key) {
      activeKeyRef.current = key;
      setState((prev) => ({
        status: prev.status === 'success' ? 'success' : 'idle',
        data: prev.data,
        error: null,
        hasPensiones: null,
      } as State));
    }

    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit.data, error: null, hasPensiones: hit.hasPensiones });
      return hit.hasPensiones;
    }

    await run(payload);

    if (activeKeyRef.current !== key) return null;

    return cacheRef.current.get(key)?.hasPensiones ?? null;
  }, [run]);

  const cancel = useCallback(() => {
    acRef.current?.abort();
  }, []);

  const loading = state.status === 'loading';
  const ok = state.status === 'success';
  const idle = state.status === 'idle';

  return useMemo(() => {
    return {
      open,
      setOpen,
      ...state,
      loading,
      ok,
      idle,
      run,
      prime,
      cancel,
    };
  }, [open, state, loading, ok, idle, run, prime, cancel]);
};