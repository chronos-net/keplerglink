// src/features/pensiones/services/pensiones.service.ts
'use client';

import axios from 'axios';
import { request } from '@/lib/apis';

/** Payload para consultar pensiones */
export type PensionesRequest = {
  neyemp: string;
  negnom?: string | null;
  rfc?: string | null;
};

/** Item devuelto por la API */
export type PensionItem = {
  clavesp: string;
  nombresp: string;

  nombrepension?: string;
  rfc?: string;
  fechaini?: string;

  tipo_desc?: string;

  altqna?: number;
  altano?: number;

  porcentaje?: number;
  importe: number;
  referencia?: string;
};

function normalizePensionesPayload(p: PensionesRequest) {
  return {
    neyemp: p.neyemp,
    // el back tolera vacío; mandamos string estable
    negnom: p.negnom?.trim() ?? '',
    rfc: p.rfc?.trim() ?? '',
  };
}

/**
 * Consulta pensiones GLINK
 * - POST /api/pensionesGLINK/consultar
 * - Retorna lista (si el back manda algo raro, regresamos [])
 * - Si se aborta por AbortController => regresamos [] (flujo normal)
 */
export async function fetchPensiones(
  payload: PensionesRequest,
  opts?: { signal?: AbortSignal }
): Promise<PensionItem[]> {
  try {
    const res = await request<PensionItem[]>({
      url: '/api/pensionesGLINK/consultar',
      method: 'POST',
      data: normalizePensionesPayload(payload),
      signal: opts?.signal,
    });

    return Array.isArray(res.data) ? res.data : [];
  } catch (err: unknown) {
    // Abort (Axios v1 usa CanceledError)
    if (axios.isCancel(err)) return [];

    // Algunas implementaciones mandan AbortError
    if (err instanceof DOMException && err.name === 'AbortError') return [];

    // Error real
    console.error('[PENSIONES][ERROR]', err);
    throw err;
  }
}
