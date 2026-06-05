'use client';

import { useCallback, useEffect, useRef, useState } from 'react';
import { fetchPensiones, PensionItem } from '../services/pensiones.service';

export function usePensiones() {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [items, setItems] = useState<PensionItem[]>([]);
  const [hasPensiones, setHasPensiones] = useState(false);

  const abortRef = useRef<AbortController | null>(null);

  const cancelarPrevio = () => {
    abortRef.current?.abort();
    abortRef.current = new AbortController();
    return abortRef.current.signal;
  };

  /** PREVIEW */
  const loadPensionesPreview = useCallback(async ({ neyemp }: { neyemp: string }) => {
    if (!neyemp) return;

    const signal = cancelarPrevio();

    setLoading(true);
    setError(null);

    try {
      const arr = await fetchPensiones({ neyemp }, { signal });
      setHasPensiones(arr.length > 0);
    } catch {
      setHasPensiones(false);
    } finally {
      setLoading(false);
    }
  }, []);

  /** FULL */
  const loadPensionesFull = useCallback(async ({ neyemp }: { neyemp: string }) => {
    if (!neyemp) return;

    const signal = cancelarPrevio();

    setOpen(true);
    setLoading(true);
    setError(null);

    try {
      const arr = await fetchPensiones({ neyemp }, { signal });
      setItems(arr);
      setHasPensiones(arr.length > 0);
    } catch {
      setError('No se pudieron cargar las pensiones.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    return () => abortRef.current?.abort();
  }, []);

  return {
    open,
    setOpen,
    loading,
    error,
    items,
    hasPensiones,
    loadPensionesPreview,
    loadPensionesFull,
  };
}
