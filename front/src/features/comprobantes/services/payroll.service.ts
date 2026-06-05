// src/features/comprobantes/services/payroll.service.ts
import { request } from '@/lib/apis';

import type { ReciboRequest } from '../types/payroll.types';
import { normalizeComprobante, RawComprobanteResponse } from '../types/comprobantes.types';

export type ServidorLite = {
  negnom: string; // nombre completo
  neyemp: string; // número de servidor público
};

export const PayrollService = {
  async generarRecibo(
    payload: ReciboRequest,
    opts?: { signal?: AbortSignal }
  ) {
    const res = await request<RawComprobanteResponse>({
      url: '/api/recibos/validacionRecibo',
      method: 'POST',
      data: payload,
      signal: opts?.signal,
    });

    return normalizeComprobante(res.data);
  },

  // 🔎 buscador por nombre (Kiliant)
  async buscarServidoresPorNombre(
    q: string,
    opts?: { signal?: AbortSignal }
  ): Promise<ServidorLite[]> {
    const term = q.trim();
    if (!term) return [];

    const res = await request<ServidorLite[]>({
      url: '/api/buscadorKiliant',
      method: 'GET',
      params: { q: term },
      signal: opts?.signal,
    });

    return Array.isArray(res.data) ? res.data : [];
  },
};
