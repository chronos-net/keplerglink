import { request } from '@/lib/apis';
import type {
  HistDavLite,
  HistoricoDavsQuery,
  HistoricoDavsResponse,
  HistoricoDavsResponseNormalized,
} from '../types/historicoDavs.types';

import { normalizeHistoricoDavsResponse } from '../types/historicoDavs.types';
import { DavsAsesoriaResponse, normalizeDavsAsesoria, RawDavsAsesoria, RequestDevsAsesoria } from '../types/historicoAsesoria.get.types';
import { DavsEntregadoResponse, normalizeDavsEntregado, RawDavsEntregado, RequestDevsEntregados } from '../types/historicoEntregados.types';
import { DavsTramiteResponse, normalizeDavsTramite, RawDavsTramite, RequestDavsTramite } from '../types/historicoTramite.get.types';
import { DevsSolicitudResponse, normalizeSolicitudDetalle, NormalizeSolicitudMeta, RawSolicitudResponse, RequestDevsSolicitudes } from '../types/historicoSolicitud.types';

/**
 * Llama al endpoint:
 * POST /api/davs/historico
 */
export async function consultarHistoricoDavs(
  payload: HistoricoDavsQuery,
  signal?: AbortSignal
): Promise<HistoricoDavsResponseNormalized> {
  const res = await request<HistoricoDavsResponse>({
    method: 'POST',
    url: '/api/davs/historico',
    data: payload,
    signal,
  });

  return normalizeHistoricoDavsResponse(res.data ?? null);
}


export const buscadorHistoricoDavs = async (
  q: string,
  opts?: { signal?: AbortSignal }
): Promise<HistDavLite[]> => {
  const term = (q ?? '').trim();
  if (!term) return [];

  const res = await request<HistDavLite[]>({
    url: '/api/buscadorHistoricoDavs',
    method: 'GET',
    params: { q: term },
    signal: opts?.signal,
  });

  const list = Array.isArray(res.data) ? res.data : [];

  // ✅ Dedup: evita claves repetidas en el autocomplete (key/id)
  const seen = new Set<string>();
  const uniq: HistDavLite[] = [];

  for (const it of list) {
    const neyemp = (it.neyemp ?? '').trim();
    const negnom = (it.negnom ?? '').trim();

    // por si viene algo raro
    if (!neyemp && !negnom) continue;

    const key = `${neyemp}|${negnom.toUpperCase()}`;
    if (seen.has(key)) continue;

    seen.add(key);
    uniq.push({ neyemp, negnom });
  }

  return uniq;
};



export const getDavsAsesoria = async (
  params: RequestDevsAsesoria,
  opts?: { signal?: AbortSignal }

): Promise<DavsAsesoriaResponse> => {

  const res = await request<RawDavsAsesoria>({
    url: `/api/davs/asesoria/${params.cveKdm1}/${params.neyemp}`,
    method: 'GET',
    signal: opts?.signal,
  })
  return normalizeDavsAsesoria(res.data, params);

}


export const getDavsEntregados = async (
  params: RequestDevsEntregados,
  opts?: { signal?: AbortSignal }

): Promise<DavsEntregadoResponse> => {
  const res = await request<RawDavsEntregado>({
    url: `/api/davs/entregado/${params.cveKdm1}`,
    method: 'GET',
    signal: opts?.signal
  })
  return normalizeDavsEntregado(res.data, params);
}


export const getDavsTramite = async (
  params: RequestDavsTramite,
  opts?: { signal?: AbortSignal }

): Promise<DavsTramiteResponse> => {

  const res = await request<RawDavsTramite>({
    url: `/api/davs/tramite/${params.cveKdm1}`,
    method: 'GET',
    signal: opts?.signal
  })
  return normalizeDavsTramite(res.data, params);

}


export const getDavsSolicitud = async (
  params: RequestDevsSolicitudes,
  meta: NormalizeSolicitudMeta,
  opts?: { signal?: AbortSignal }
): Promise<DevsSolicitudResponse> => {
  const res = await request<RawSolicitudResponse>({
    url: `/api/davs/solicitudes/${params.cveKdm1}/${params.neyemp}/${params.destinatarioCheque}`,
    method: 'GET',
    signal: opts?.signal
  });

  return normalizeSolicitudDetalle(res.data, meta);
};

