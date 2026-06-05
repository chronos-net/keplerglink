'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';

import type {
  HistoricoDavsQuery,
  HistoricoDavsResponseNormalized,
  HistoricoDavsTipo,
} from '../types/historicoDavs.types';

import { consultarHistoricoDavs } from '../services/historico-davs.service';

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

type State =
  | { status: 'idle'; data: HistoricoDavsResponseNormalized | null; error: null }
  | { status: 'loading'; data: HistoricoDavsResponseNormalized | null; error: null }
  | { status: 'success'; data: HistoricoDavsResponseNormalized; error: null }
  | { status: 'error'; data: HistoricoDavsResponseNormalized | null; error: string };

type CacheKey = string;

const makeCacheKey = (p: HistoricoDavsQuery): CacheKey => {
  const ney = (p.neyemp ?? '').trim();
  const nom = (p.negnom ?? '').trim().toUpperCase();

  if (ney) return `NEYEMP:${ney}`;
  return `NOMBRE:${nom}`;
};

function mergeAll(data: HistoricoDavsResponseNormalized) {
  // “todos” = merge de 4 listas, con tipo incluido para pintar badge y tab
  const withTipo = <T extends HistoricoDavsTipo>(
    tipo: T,
    rows: HistoricoDavsResponseNormalized[T]
  ) => rows.map((r) => ({ ...r, tipo }));

  const all = [
    ...withTipo('asesoria', data.asesoria),
    ...withTipo('tramite', data.tramite),
    ...withTipo('solicitud', data.solicitud),
    ...withTipo('entrega', data.entrega),
  ];

  // orden por fecha desc (ISO yyyy-mm-dd => comparable con Date)
  all.sort((a, b) => new Date(b.fecha).getTime() - new Date(a.fecha).getTime());

  return all;
}

type TabKey = 'todos' | HistoricoDavsTipo;

export function useHistoricoDavs() {
  const [state, setState] = useState<State>({ status: 'idle', data: null, error: null });

  const acRef = useRef<AbortController | null>(null);
  const cacheRef = useRef(new Map<CacheKey, HistoricoDavsResponseNormalized>());

  // UI state (tab + buscador de folio)
  const [tab, setTab] = useState<TabKey>('todos');
  const [folioQ, setFolioQ] = useState('');

  useEffect(() => {
    return () => acRef.current?.abort();
  }, []);

  const consultar = useCallback(async (payload: HistoricoDavsQuery) => {
    // validación mínima (sin romper UX)
    const ney = (payload.neyemp ?? '').trim();
    const nom = (payload.negnom ?? '').trim();
    if (!ney && !nom) {
      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'Proporciona al menos neyemp o negnom.',
      }));
      return null;
    }

    // normaliza payload como en anualizado
    const normalized: HistoricoDavsQuery = ney
      ? { neyemp: ney }
      : { negnom: nom.toUpperCase() };

    const key = makeCacheKey(normalized);

    // 0) cache-first (si es misma búsqueda, NO loading, NO parpadeos)
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
      const data = await consultarHistoricoDavs(normalized, ac.signal);

      cacheRef.current.set(key, data);
      setState({ status: 'success', data, error: null });
      return data;
    } catch (err) {
      if (isAbortLike(err)) return null;

      setState((prev) => ({
        status: 'error',
        data: prev.data,
        error: 'No se pudo consultar el histórico DAVS. Intenta de nuevo.',
      }));
      return null;
    }
  }, []);

  const cancel = useCallback(() => {
    acRef.current?.abort();
  }, []);

  // =========================
  // Derivados para UI
  // =========================
  const loading = state.status === 'loading';
  const error = state.status === 'error' ? state.error : null;

  const hasData = !!state.data;
  const counts = useMemo(() => {
    const d = state.data;
    const base = { todos: 0, asesoria: 0, tramite: 0, solicitud: 0, entrega: 0 };

    if (!d) return base;

    base.asesoria = d.asesoria.length;
    base.tramite = d.tramite.length;
    base.solicitud = d.solicitud.length;
    base.entrega = d.entrega.length;
    base.todos = base.asesoria + base.tramite + base.solicitud + base.entrega;

    return base;
  }, [state.data]);

  const rows = useMemo(() => {
    const d = state.data;
    if (!d) return [];

    // 1) base list por tab
    const all = mergeAll(d);

    const byTab =
      tab === 'todos' ? all : all.filter((r) => r.tipo === tab);

    // 2) filtro por folio
    const q = folioQ.trim().toLowerCase();
    if (!q) return byTab;

    return byTab.filter((r) => (r.folioDocumento ?? '').toLowerCase().includes(q));
  }, [state.data, tab, folioQ]);

  return useMemo(() => {
    return {
      // core
      consultar,
      cancel,
      loading,
      error,

      // ui state
      rows,
      tab,
      setTab,
      folioQ,
      setFolioQ,
      counts,

      // flags para tu page
      hasData
    };
  }, [
    consultar,
    cancel,
    loading,
    error,
    rows,
    tab,
    folioQ,
    counts,
    hasData
  ]);
}