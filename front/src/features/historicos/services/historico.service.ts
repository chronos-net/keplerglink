// src/features/historicos/services/historico.api.ts
'use client';
import { request } from '@/lib/apis';

import type {
  HistoricoRequest,
  RawHistoricoResponse,
  HistoricoResponse,
  HistoricoLite,
} from '../types/historico.types';
import { normalizeHistorico } from '../types/historico.types';

/**
 * POST /api/vistaHistorico/validacionHistorico
 * - Valida y devuelve histórico (normalizado a modelo UI)
 */
export async function postHistoricoGlink(
  payload: HistoricoRequest,
  opts?: { signal?: AbortSignal }
): Promise<HistoricoResponse> {
  const res = await request<RawHistoricoResponse>({
    url: '/api/vistaHistorico/validacionHistorico',
    method: 'POST',
    data: payload,
    signal: opts?.signal,
  });

  return normalizeHistorico(res.data);
}

export const buscadorHistoricoGlink = async (
  q: string,
  opts?: { signal?: AbortSignal }

): Promise<HistoricoLite[]> => {

  const term = (q ?? '').trim();
  if (!term) return []

  const res = await request<HistoricoLite[]>({
    url: '/api/buscadorHistoricoGlink',
    method: 'GET',
    params: { q: term },
    signal: opts?.signal,
  });
  return Array.isArray(res.data) ? res.data : [];
}
