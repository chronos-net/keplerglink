'use client';

import axios from 'axios';
import { request } from '@/lib/apis';

import type { PrestamosRaw, PrestamosResponse, PrestamosRequest } from '../types/prestamos.types';
import { normalizePrestamos } from '../types/prestamos.types';

import type { ServidorLite } from '@/features/comprobantes/services/payroll.service';

/**
 * POST /api/vistaPrestamos/validacionPrestamos
 * - Valida y devuelve préstamos (normalizado a modelo UI)
 */
export async function postValidacionPrestamos(
  payload: PrestamosRequest,
  opts?: { signal?: AbortSignal }
): Promise<PrestamosResponse> {
  try {
    const res = await request<PrestamosRaw>({
      url: '/api/vistaPrestamos/validacionPrestamos',
      method: 'POST',
      data: payload,
      signal: opts?.signal,
    });

    return normalizePrestamos(res.data);
  } catch (err: unknown) {
    if (axios.isCancel(err)) throw err;
    console.error('[PRESTAMOS][ERROR]', err);
    throw err;
  }
}

/**
 * GET /api/buscadorPrestamosGlink
 * - Autocomplete para Préstamos GLINK
 */
export async function buscarPrestamosGlink(
  q: string,
  opts?: { signal?: AbortSignal }
): Promise<ServidorLite[]> {
  
  const term = (q ?? '').trim();
  if (!term) return [];

  const res = await request<ServidorLite[]>({
    url: '/api/buscadorPrestamosGlink',
    method: 'GET',
    params: { q: term },
    signal: opts?.signal,
  });

  return Array.isArray(res.data) ? res.data : [];
}