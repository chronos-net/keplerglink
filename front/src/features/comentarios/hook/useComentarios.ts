'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import type { GetComentariosRequest, GetComentariosResponse } from '../types/comentarios.get.types';
import { getComments } from '../services/comentarios.service';

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

/** -----------------------------
 *  Error helpers (LOCAL, sin utils globales, sin any)
 *  ----------------------------- */
type ApiErr = {
  response?: {
    status?: number;
    data?: unknown;
  };
  message?: unknown;
};

const isApiErr = (e: unknown): e is ApiErr => typeof e === 'object' && e !== null;

const getErrStatus = (e: unknown): number | null => {
  const s = isApiErr(e) ? e.response?.status : undefined;
  return typeof s === 'number' ? s : null;
};

const getErrMessage = (e: unknown): string => {
  if (!isApiErr(e)) return '';

  const data = e.response?.data;

  // backend típico: { message: "..." }
  if (typeof data === 'object' && data !== null && 'message' in data) {
    const m = (data as { message?: unknown }).message;
    return typeof m === 'string' ? m : '';
  }

  // fallback
  return typeof e.message === 'string' ? e.message : '';
};

const isNotFoundAs500 = (e: unknown): boolean => {
  const status = getErrStatus(e);
  const msg = getErrMessage(e).toLowerCase();

  return (
    status === 500 &&
    (msg.includes('404_not_found') || msg.includes('no se encontró la cabecera'))
  );
};

/** -----------------------------
 *  State + Cache
 *  ----------------------------- */
type State =
  | { status: 'idle'; error: null; data: GetComentariosResponse | null; hasComments: boolean | null }
  | { status: 'loading'; error: null; data: GetComentariosResponse | null; hasComments: boolean | null }
  | { status: 'success'; error: null; data: GetComentariosResponse; hasComments: boolean }
  | { status: 'error'; error: string; data: GetComentariosResponse | null; hasComments: boolean | null };

type CacheKey = string;

const makeCacheKey = (p: GetComentariosRequest): CacheKey => {
  const anio = String(p.anio);
  const periodo = String(p.periodo).trim();
  const ney = String(p.neyemp ?? '').trim();

  return `${anio}::${periodo}::NEYEMP:${ney}`;
};

type CacheValue = {
  data: GetComentariosResponse;
  hasComments: boolean;
};

export const useComments = () => {
  const [open, setOpen] = useState(false);

  const [state, setState] = useState<State>({
    status: 'idle',
    error: null,
    data: null,
    hasComments: null,
  });

  const acRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, CacheValue>());

  // último key activo (anti-race)
  const activeKeyRef = useRef<CacheKey | null>(null);

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const run = useCallback(async (payload: GetComentariosRequest) => {
    if (!payload?.anio || !payload?.periodo || !payload.neyemp) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'Proporciona anio, periodo y neyemp.',
        hasComments: prev.hasComments,
      }));
      return null;
    }

    const key = makeCacheKey(payload);
    activeKeyRef.current = key;

    // cache-first
    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit.data, error: null, hasComments: hit.hasComments });
      return hit.data;
    }

    // abort anterior
    acRef.current?.abort();
    const ac = new AbortController();
    acRef.current = ac;

    // loading sin vaciar data previa
    setState((prev) => ({
      status: 'loading',
      data: prev.data,
      error: null,
      hasComments: prev.hasComments,
    }));

    try {
      const data = await getComments(payload, { signal: ac.signal });

      // si cambió el key, ignorar resultado viejo
      if (activeKeyRef.current !== key) return null;

      const valores = data.valores ?? [];
      const has = valores.some(
        (item) => (item?.comentario ?? '').trim().length > 0
      );

      const normalized: GetComentariosResponse = {
        ...data,
        valores,
      };

      cacheRef.current.set(key, { data: normalized, hasComments: has });
      setState({ status: 'success', data: normalized, error: null, hasComments: has });

      return normalized;
    } catch (err: unknown) {
      if (isAbortLike(err)) return null;
      if (activeKeyRef.current !== key) return null;

      // Backend mal: 500 pero realmente NOT_FOUND (no hay cabecera)
      if (isNotFoundAs500(err)) {
        const empty: GetComentariosResponse = {
          cabecera: {
            neyemp: payload.neyemp ?? '',
            nombreCompleto: '',
          },
          valores: [],
        };

        cacheRef.current.set(key, { data: empty, hasComments: false });
        setState({ status: 'success', data: empty, error: null, hasComments: false });
        return empty;
      }

      // error real
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'No se pudieron obtener comentarios.',
        hasComments: prev.hasComments,
      }));
      return null;
    }
  }, []);

  const prime = useCallback(async (payload: GetComentariosRequest) => {
    // payload inválido -> desconocido
    if (!payload?.anio || !payload?.periodo || !payload.neyemp) {
      activeKeyRef.current = null;
      setState((prev) => ({
        status: prev.status === 'success' ? 'success' : 'idle',
        data: prev.data,
        error: null,
        hasComments: null,
      } as State));
      return null;
    }

    const key = makeCacheKey(payload);

    // cambia contexto -> reset badge
    if (activeKeyRef.current !== key) {
      activeKeyRef.current = key;
      setState((prev) => ({
        status: prev.status === 'success' ? 'success' : 'idle',
        data: prev.data,
        error: null,
        hasComments: null,
      } as State));
    }

    // cache-first
    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit.data, error: null, hasComments: hit.hasComments });
      return hit.hasComments;
    }

    // si no hay cache, usa run
    await run(payload);

    // si cambió contexto mientras esperaba, ignora
    if (activeKeyRef.current !== key) return null;

    // devuelve el hasComments real (ya sea success o vacío)
    return cacheRef.current.get(key)?.hasComments ?? null;
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
