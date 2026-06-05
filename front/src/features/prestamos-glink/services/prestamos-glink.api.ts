// src/features/prestamos-glink/services/prestamos-glink.api.ts
'use client';

import axios from 'axios';
import { request } from '@/lib/apis';

import type {
  PrestamosGlinkRequest,
  PrestamosGlinkResponse,
} from '../types/prestamos-glink.types';

/**
 * POST /api/vistaPrestamos/validacionPrestamos
 * - Devuelve cabecera + lista de préstamos GLINK
 */
export async function postPrestamosGlink(
  payload: PrestamosGlinkRequest,
  opts?: { signal?: AbortSignal }
): Promise<PrestamosGlinkResponse> {
  try {
    const res = await request<PrestamosGlinkResponse>({
      url: '/api/vistaPrestamos/validacionPrestamos',
      method: 'POST',
      data: payload,
      signal: opts?.signal,
    });

    // Seguridad mínima (por si el back manda null/undefined)
    return {
      cabeceraPrestamos: res.data?.cabeceraPrestamos ?? ({} as PrestamosGlinkResponse['cabeceraPrestamos']),
      descPrestamos: Array.isArray(res.data?.descPrestamos) ? res.data.descPrestamos : [],
    };
  } catch (err: unknown) {
    if (axios.isCancel(err)) throw err;
    console.error('[PRESTAMOS_GLINK][ERROR]', err);
    throw err;
  }
}
