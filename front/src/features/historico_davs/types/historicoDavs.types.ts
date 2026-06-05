// historicoDavs.types.ts
import type { DavsAsesoriaResponse } from './historicoAsesoria.get.types';
import { DavsEntregadoResponse } from './historicoEntregados.types';
import { DevsSolicitudResponse } from './historicoSolicitud.types';
import { DavsTramiteResponse } from './historicoTramite.get.types';

export type HistDavLite = {
    neyemp: string;
    negnom: string;
};

export type HistoricoDavsQuery =
    | { neyemp: string; negnom?: never }
    | { negnom: string; neyemp?: never };

export type HistoricoDavsTipo = 'asesoria' | 'tramite' | 'solicitud' | 'entrega';


export type HistoricoDavsItem = {
    cveKdm1: number;
    folioDocumento: string;
    neyemp: string;
    negnom: string;
    fecha: string;
    destinatarioCheque?: string;
}
export type HistoricoDavsResponse = Partial<Record<HistoricoDavsTipo, HistoricoDavsItem[]>>;

export type HistoricoDavsResponseNormalized = Record<HistoricoDavsTipo, HistoricoDavsItem[]>;

export type HistoricoDavsTab = 'todos' | HistoricoDavsTipo;

export type HistoricoDavsCounts = Record<HistoricoDavsTab, number>;

export type HistoricoDavsTableRow = HistoricoDavsItem & {
    tipo: HistoricoDavsTipo;
};

export type Tabfilter = {
    value: HistoricoDavsTab;
    label: string;
}

export type DavsDetailKind =
    | 'ASESORIA'
    | 'TRAMITE'
    | 'SOLICITUD'
    | 'ENTREGADO';


export type DavsDetailRow = {
    tipo: string;
    cveKdm1: number;
    neyemp: string;
    destinatarioCheque?: string | null;
};

export type DavsDetailData =
    | DavsAsesoriaResponse
    | DavsEntregadoResponse
    | DavsTramiteResponse
    | DevsSolicitudResponse
    | null;

export function normalizeHistoricoDavsResponse(
    raw: HistoricoDavsResponse | null | undefined
): HistoricoDavsResponseNormalized {
    return {
        asesoria: raw?.asesoria ?? [],
        tramite: raw?.tramite ?? [],
        solicitud: raw?.solicitud ?? [],
        entrega: raw?.entrega ?? [],
    };
}

export function splitNombreOClave(text: string): { neyemp: string | null; negnom: string | null } {
    const t = text.trim();
    if (!t) return { neyemp: null, negnom: null };

    const compact = t.replace(/\s+/g, '');
    if (/^\d{6,}$/.test(compact)) return { neyemp: compact, negnom: null };

    return { neyemp: null, negnom: t.toUpperCase() };
}

export function hasAnyResult(data: HistoricoDavsResponseNormalized): boolean {
    return (
        data.asesoria.length > 0 ||
        data.tramite.length > 0 ||
        data.solicitud.length > 0 ||
        data.entrega.length > 0
    );
}