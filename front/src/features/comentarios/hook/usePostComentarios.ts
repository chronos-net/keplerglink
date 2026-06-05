'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import type {
  PostCommentsRequest,
  PostCommentsResponse,
} from '../types/comentarios.post.types';
import { postComments } from '../services/comentarios.service';
import axios from 'axios';

type State =
  | { status: 'idle'; data: null; error: null }
  | { status: 'loading'; data: PostCommentsResponse | null; error: null }
  | { status: 'success'; data: PostCommentsResponse; error: null }
  | { status: 'error'; data: PostCommentsResponse | null; error: string };

type CacheKey = string;

const makeCacheKey = (params: PostCommentsRequest): CacheKey => {
  return params.neyemp.trim().toUpperCase();
};

const validatePayload = (params: PostCommentsRequest): string | null => {
  if (!params.neyemp.trim()) {
    return 'Captura la clave del servidor público.';
  }

  return null;
};

const isAbortLike = (err: unknown) => {
  if (typeof err !== 'object' || !err) return false;

  const e = err as { name?: string; code?: string; message?: string };

  return (
    e.name === 'AbortError' ||
    e.code === 'ERR_CANCELED' ||
    e.name === 'CanceledError' ||
    (e.message ?? '').toLowerCase().includes('canceled')
  );
};

export const usePostComentarios = () => {
  const [state, setState] = useState<State>({
    status: 'idle',
    data: null,
    error: null,
  });

  const abortRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, PostCommentsResponse>());

  useEffect(() => {
    return () => abortRef.current?.abort();
  }, []);

  const cancel = useCallback(() => {
    abortRef.current?.abort();
    abortRef.current = null;
  }, []);

  const run = useCallback(async (params: PostCommentsRequest) => {
    const validationError = validatePayload(params);

    if (validationError) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: validationError,
      }));

      return null;
    }

    const payload: PostCommentsRequest = {
      neyemp: params.neyemp.trim(),
    };

    const cacheKey = makeCacheKey(payload);
    const cachedData = cacheRef.current.get(cacheKey);

    if (cachedData) {
      setState({
        status: 'success',
        data: cachedData,
        error: null,
      });

      return cachedData;
    }

    abortRef.current?.abort();

    const controller = new AbortController();
    abortRef.current = controller;

    setState((prev) => ({
      status: 'loading',
      data: prev.data,
      error: null,
    }));

    try {
      const response = await postComments(payload, {
        signal: controller.signal,
      });

      cacheRef.current.set(cacheKey, response);

      setState({
        status: 'success',
        data: response,
        error: null,
      });

      return response;
    } catch (err: unknown) {
      if (isAbortLike(err)) return null;

      let message = 'No se pudieron consultar los comentarios.';

      if (axios.isAxiosError(err)) {
        const status = err.response?.status;

        if (status === 404) {
          message =
            'No encontramos comentarios para la clave capturada. Verifica la información e intenta nuevamente.';
        } else if (status === 500) {
          message =
            'Ocurrió un problema al consultar los comentarios. Intenta nuevamente más tarde.';
        } else if (!err.response) {
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

  const reset = useCallback(() => {
    abortRef.current?.abort();
    abortRef.current = null;

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
    const header = state.data?.cabesera ?? null;
    const items = state.data?.valores ?? [];

    return {
      ...state,
      loading,
      ok,
      idle,
      header,
      items,
      run,
      cancel,
      reset,
      hasData: !!state.data,
      hasItems: items.length > 0,
      isEmpty: ok && items.length === 0,
    };
  }, [state, loading, ok, idle, run, cancel, reset])}