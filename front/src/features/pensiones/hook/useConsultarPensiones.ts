'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import type { DataPensiones, PensionesGlinkRequest, StatsPensiones } from '../types/pensiones.types';
import { consultarPensionesGlink } from '../services/pensiones.service';

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

type ViewData = DataPensiones & { stats: StatsPensiones };

type State =
  | { status: 'idle'; error: null; data: ViewData | null }
  | { status: 'loading'; error: null; data: ViewData | null }
  | { status: 'success'; error: null; data: ViewData }
  | { status: 'error'; error: string; data: ViewData | null };

type CacheKey = string;

const makeCacheKey = (p: PensionesGlinkRequest): CacheKey => {
  const ney = (p.neyemp ?? '').trim();
  return `GLINK::NEYEMP:${ney}`;
};

type CacheValue = {
  data: ViewData;
};

const buildStats = (data: DataPensiones): StatsPensiones => {
  const list = data.pensiones ?? [];
  const totalPensiones = list.length;

  if (!totalPensiones) {
    return { totalPensiones: 0, importeTotal: 0, porcentajePromedio: 0 };
  }

  const importeTotal = list.reduce((acc, x) => acc + (x.importe ?? 0), 0);
  const porcentajePromedio =
    list.reduce((acc, x) => acc + (x.porcentaje ?? 0), 0) / totalPensiones;

  return { totalPensiones, importeTotal, porcentajePromedio };
};

export const useConsultarPensiones = () => {
  const [state, setState] = useState<State>({
    status: 'idle',
    error: null,
    data: null,
  });

  const acRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, CacheValue>());
  const activeKeyRef = useRef<CacheKey | null>(null);

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const run = useCallback(async (payload: PensionesGlinkRequest) => {
    const neyemp = (payload?.neyemp ?? '').trim();
    if (!neyemp) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'Proporciona neyemp.',
      }));
      return null;
    }

    const cleanPayload: PensionesGlinkRequest = { neyemp };
    const key = makeCacheKey(cleanPayload);
    activeKeyRef.current = key;

    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit.data, error: null });
      return hit.data;
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
      const data = await consultarPensionesGlink(cleanPayload, { signal: ac.signal });

      if (activeKeyRef.current !== key) return null;

      const view: ViewData = {
        ...data,
        stats: buildStats(data),
      };

      cacheRef.current.set(key, { data: view });
      setState({ status: 'success', data: view, error: null });

      return view;
    } catch (err: unknown) {
      if (isAbortLike(err)) return null;
      if (activeKeyRef.current !== key) return null;

      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'No se pudieron obtener pensiones GLINK.',
      }));

      return null;
    }
  }, []);

  // opcional: prime (igual que tu hook viejo)
  const prime = useCallback(async (payload: PensionesGlinkRequest) => {
    const neyemp = (payload?.neyemp ?? '').trim();
    if (!neyemp) {
      activeKeyRef.current = null;
      setState((prev) => ({
        status: prev.status === 'success' ? 'success' : 'idle',
        data: prev.data,
        error: null,
      } as State));
      return null;
    }

    const key = makeCacheKey({ neyemp });

    if (activeKeyRef.current !== key) {
      activeKeyRef.current = key;
      setState((prev) => ({
        status: prev.status === 'success' ? 'success' : 'idle',
        data: prev.data,
        error: null,
      } as State));
    }

    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit.data, error: null });
      return hit.data;
    }

    return await run({ neyemp });
  }, [run]);

  const cancel = useCallback(() => {
    acRef.current?.abort();
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
      run,
      prime,
      cancel,
    };
  }, [state, loading, ok, idle, run, prime, cancel]);
};