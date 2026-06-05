// src/features/comprobantes/services/catalogos.service.ts
import { request } from '@/lib/apis';

export type AnioDTO = {
  cveAnio: number;
  descAnio: string; // "1996", "1997", ...
};

export type PeriodoDTO = {
  cvePeriodo: number;
  descPeriodo: string; // "01", "02", "Q1", etc.
};

type AnioResponse = {
  anio?: AnioDTO[];
  anios?: AnioDTO[]; // fallback por si cambian llave
};

type PeriodoResponse = {
  periodo?: PeriodoDTO[];
  periodos?: PeriodoDTO[]; // fallback por si cambian llave
};

export const CatalogosService = {
  async getAnios(opts?: { signal?: AbortSignal }): Promise<AnioDTO[]> {
    const res = await request<AnioResponse>({
      url: '/api/anio/catalogo',
      method: 'GET',
      signal: opts?.signal,
    });

    return res.data?.anio ?? res.data?.anios ?? [];
  },

  async getPeriodos(opts?: { signal?: AbortSignal }): Promise<PeriodoDTO[]> {
    const res = await request<PeriodoResponse>({
      url: '/api/periodo/catalogo',
      method: 'GET',
      signal: opts?.signal,
    });

    return res.data?.periodo ?? res.data?.periodos ?? [];
  },
};
