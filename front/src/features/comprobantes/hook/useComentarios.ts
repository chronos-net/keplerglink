'use client';

import { useState, useRef } from 'react';
import { fetchComments, type CommentItem } from '@/features/comprobantes/services/comentarios.service';
import { getHttpErrorMessage, isCanceledError } from '@/utils/http-error';

export function useComments() {
  const [open, setOpen] = useState(false);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [items, setItems] = useState<CommentItem[]>([]);
  const [hasComments, setHasComments] = useState(false);

  const abortRef = useRef<AbortController | null>(null);

  async function loadPreview(args: { neyemp?: string; periodo: string; anio: number | string }) {
    const { neyemp, periodo, anio } = args;

    if (!neyemp) return;

    try {
      const data = await fetchComments({ neyemp, periodo, anio });
      const cleaned = (data?.comentarios || []).filter(
        (c) => (c?.comentario || '').trim().length > 0
      );

      setHasComments(cleaned.length > 0);
    } catch {
      setHasComments(false);
    }
  }

  async function loadFull(args: { neyemp?: string; periodo: string; anio: number | string }) {
    const { neyemp, periodo, anio } = args;

    if (!neyemp) return;

    abortRef.current?.abort();
    const ctrl = new AbortController();
    abortRef.current = ctrl;

    setLoading(true);
    setError(null);

    try {
      const data = await fetchComments({ neyemp, periodo, anio }, { signal: ctrl.signal });
      const cleaned = (data?.comentarios || []).filter(
        (c) => (c?.comentario || '').trim().length > 0
      );

      setItems(cleaned);
    } catch (e: unknown) {
      if (isCanceledError(e)) return;
      setError(getHttpErrorMessage(e, 'No se pudieron obtener comentarios.'));
    } finally {
      setLoading(false);
    }
  }

  return {
    open,
    setOpen,
    loading,
    error,
    items,
    hasComments,
    loadPreview,
    loadFull,
  };
}