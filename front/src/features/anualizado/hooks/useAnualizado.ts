'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { AnualizadoRequest, AnualizadoResponse } from '../types/anualizado.dto';
import { postValidacionesAnualizado } from '../service/anualizado.service';
import axios from 'axios';


type State =
  | { status: 'idle'; data: null; error: null }
  | { status: 'loading'; data: AnualizadoResponse | null; error: null }
  | { status: 'success'; data: AnualizadoResponse; error: null }
  | { status: 'error'; data: null; error: string };

type CacheKey = string;

const makeCacheKey = (p: AnualizadoRequest): CacheKey => {
  const anio = String(p.anio);
  const ney = (p.neyemp ?? '').trim();
  const nom = (p.nombreSp ?? '').trim().toUpperCase();

  if (ney) return `${anio}::NEYEMP:${ney}`;
  return `${anio}::NOMBRE:${nom}`;
}

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
}

export function useAnualizado() {
  const [state, setState] = useState<State>({ status: 'idle', data: null, error: null });

  const acRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, AnualizadoResponse>());
  const requestSeqRef = useRef(0);

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const run = useCallback(async (payload: AnualizadoRequest) => {
    // validación mínima (sin romper UX)
    if (!payload?.anio || (!payload.neyemp && !payload.nombreSp)) {
      setState({
        status: 'error',
        data: null,
        error: 'Proporciona al menos anio y (neyemp o nombreSp).',
      });
      return null;
    }

    const key = makeCacheKey(payload);

    // Invalida respuestas tardías de búsquedas anteriores
    acRef.current?.abort();
    const requestSeq = ++requestSeqRef.current;

    // 0) cache-first (solo key exacta)
    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit, error: null });
      return hit;
    }

    // 1) nuevo controller
    const ac = new AbortController();
    acRef.current = ac;

    // 2) loading limpiando data anterior
    setState({ status: 'loading', data: null, error: null });

    try {
      const data = await postValidacionesAnualizado(payload, { signal: ac.signal });

      if (requestSeq !== requestSeqRef.current) return null;

      cacheRef.current.set(key, data);
      setState({ status: 'success', data, error: null });
      return data;

    } catch (err) {

      if (isAbortLike(err)) return null;

      if (requestSeq !== requestSeqRef.current) return null;

      let message = 'No se pudo consultar el anualizado del servidor publico.'

      if (axios.isAxiosError(err)) {

        const status = err.response?.status;

        if (status === 404) {
          message = 'No encontramos datos capturados. Verifica los datos.'

        } else if (status === 500) {
          message = 'Ocurrió un error al consultar tu anualizado. Contacta a soporte'

        } else if (!err.response) {
          message = 'No fue posible conectar con el servidor.'
        }

      }

      setState({
        status: 'error',
        data: null,
        error: message,
      });
      return null;
    }
  }, []);

  const cancel = useCallback(() => {
    acRef.current?.abort();
  }, []);

  const loading = state.status === 'loading';
  const ok = state.status === 'success';
  const idle = state.status === 'idle';

  return useMemo(() => {
    const empleado = state.data?.empleado ?? null;
    const resumen = state.data?.resumenGlobal ?? null;

    return {
      ...state,
      loading,
      ok,
      idle,
      run,
      cancel,
      empleado,
      resumen,
    };
  }, [state, loading, ok, idle, run, cancel]);
}