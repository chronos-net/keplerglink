import { DavsDetailKind } from "../types/historicoDavs.types";


export const DAVS_DETAIL_TITLES: Record<Exclude<DavsDetailKind, null>, string> = {
  ASESORIA: 'Consulta Asesoría',
  TRAMITE: 'Consulta Trámite',
  SOLICITUD: 'Consulta Solicitud',
  ENTREGADO: 'Consulta Entregado',
};

export function getDavsDetailTitle(kind: DavsDetailKind | null): string {
  if (!kind) return 'Detalle DAVS';
  return DAVS_DETAIL_TITLES[kind] ?? 'Detalle DAVS';
}