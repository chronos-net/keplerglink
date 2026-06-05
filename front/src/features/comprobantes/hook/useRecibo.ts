'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import axios from 'axios';

import { PayrollService } from '../services/payroll.service';
import type { ReciboRequest } from '../types/payroll.types';
import { ComprobanteResponse } from '../types/comprobantes.types';

type State =
  | { status: 'idle'; data: null; error: null }
  | { status: 'loading'; data: ComprobanteResponse | null; error: null }
  | { status: 'success'; data: ComprobanteResponse; error: null }
  | { status: 'error'; data: ComprobanteResponse | null; error: string };


type CacheKey = string;

const makeCacheKey = (p: ReciboRequest): CacheKey => {
  // ajusta nombres según tu payload real
  const anio = String(p.anio);
  const period = String(p.quincena);
  const ney = (p.neyemp ?? '').trim();
  const nom = String(p.nombreSp ?? '').trim().toUpperCase();

  // identity estable
  if (ney) return `${anio}::${period}::NEYEMP:${ney}`;
  return `${anio}::${period}::NOMBRE:${nom}`;
}

const validatePayload = (p: ReciboRequest): string | null => {
  // ajusta validaciones a tu payload real
  const anio = p.anio;
  const periodo = p.quincena;
  const ney = p.neyemp
  const nombre = p.nombreSp

  if (!anio) return 'Proporciona el año.';
  if (!periodo) return 'Proporciona el periodo/quincena.';
  if (!ney && !nombre) return 'Proporciona neyemp o nombre.';
  return null;
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

export const useRecibo = () => {
  const [state, setState] = useState<State>({
    status: 'idle',
    data: null,
    error: null,
  });

  const acRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, ComprobanteResponse>());

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const generar = useCallback(async (payload: ReciboRequest) => {
    const v = validatePayload(payload);
    if (v) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: v,
      }));
      return null;
    }

    const key = makeCacheKey(payload);

    // 0) cache-first
    const hit = cacheRef.current.get(key);
    if (hit) {
      setState({ status: 'success', data: hit, error: null });
      return hit;
    }

    // 1) abort anterior
    acRef.current?.abort();

    // 2) nuevo controller
    const ac = new AbortController();
    acRef.current = ac;

    // 3) loading sin vaciar data previa
    setState((prev) => ({ status: 'loading', data: prev.data, error: null }));

    try {
      const data = await PayrollService.generarRecibo(payload, {
        signal: ac.signal,
      });

      cacheRef.current.set(key, data);
      setState({ status: 'success', data, error: null });
      return data;
    } catch (err: unknown) {
      if (isAbortLike(err)) return null;

      // mismo manejo de mensajes “bonitos” que tu mutation
      let message = 'No se pudo consultar el recibo del servidor Publico ';

      if (axios.isAxiosError(err)) {
        const status = err.response?.status;

        if (status === 404) {
          message =
            'No encontramos recibos para los datos capturados. Verifica la clave, el nombre, el año y la quincena.';
        } else if (status === 500) {
          message =
            'No fue posible consultar los recibos en este momento. Intenta nuevamente más tarde.';
        } else if (err.message) {
          message = err.message;
        }
      } else if (err instanceof Error && err.message) {
        message = err.message;
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
    acRef.current?.abort();
  }, []);

  const reset = useCallback(() => {
    setState({ status: 'idle', data: null, error: null });
  }, []);

  const loading = state.status === 'loading';
  const ok = state.status === 'success';
  const idle = state.status === 'idle';

  return useMemo(() => {
    const empleado = state.data?.empleado ?? null;
    const resumen = state.data?.resumen ?? null;

    return {
      ...state,
      generar,      // igual que useAnualizado
      cancel,
      reset,
      loading,
      ok,
      idle,
      empleado,
      resumen,
      hasData: !!state.data,
      isEmpty: !loading && !state.data && !state.error,
    };
  }, [state, generar, cancel, reset, loading, ok, idle]);
}