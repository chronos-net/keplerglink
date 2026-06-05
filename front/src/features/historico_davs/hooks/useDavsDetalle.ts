// src/features/historico-davs/hook/useDavsDetalle.ts
'use client';

import { useCallback, useEffect, useMemo, useRef, useState } from 'react';

import { getDavsAsesoria, getDavsEntregados, getDavsSolicitud, getDavsTramite } from '../services/historico-davs.service';
import type { DavsAsesoriaResponse } from '../types/historicoAsesoria.get.types';
import { DavsEntregadoResponse } from '../types/historicoEntregados.types';
import { DavsDetailData, DavsDetailKind, DavsDetailRow } from '../types/historicoDavs.types';
import { DavsTramiteResponse } from '../types/historicoTramite.get.types';
import { DevsSolicitudResponse } from '../types/historicoSolicitud.types';

function isAbortLike(err: unknown) {
    if (typeof err === 'object' && err) {
        const e = err as { name?: string; code?: string; message?: string };
        return (
            e.name === 'AbortError' ||
            e.code === 'ERR_CANCELED' ||
            e.name === 'CanceledError' ||
            (e.message ?? '').toLowerCase().includes('canceled')
        );
    }
    return false;
}

type State =
    | {
        status: 'idle';
        open: false;
        kind: null;
        data: null;
        error: null;
        activeKey: null;
    }
    | {
        status: 'loading';
        open: true;
        kind: DavsDetailKind;
        data: DavsDetailData;
        error: null;
        activeKey: string;
    }
    | {
        status: 'success';
        open: true;
        kind: DavsDetailKind;
        data: DavsDetailData;
        error: null;
        activeKey: string;
    }
    | {
        status: 'error';
        open: true;
        kind: DavsDetailKind;
        data: DavsDetailData;
        error: string;
        activeKey: string;
    };

type CacheValue = {
    kind: DavsDetailKind;
    data: DavsDetailData;
};

function normalizeKind(tipo: string): DavsDetailKind | null {
    const t = (tipo ?? '').trim().toUpperCase();

    if (t === 'ASESORIA' || t === 'ASESORÍA') return 'ASESORIA';
    if (t === 'TRAMITE' || t === 'TRÁMITE') return 'TRAMITE';
    if (t === 'SOLICITUD') return 'SOLICITUD';
    if (t === 'ENTREGADO' || t === 'ENTREGA') return 'ENTREGADO';

    return null;
}
function makeDetailKey(row: DavsDetailRow, kind: DavsDetailKind): string {
    const destinatario = (row.destinatarioCheque ?? '').trim();

    switch (kind) {
        case 'ASESORIA':
            return `ASESORIA::${row.cveKdm1}::${row.neyemp}`;
        case 'TRAMITE':
            return `TRAMITE::${row.cveKdm1}::${row.neyemp}`;
        case 'SOLICITUD':
            return `SOLICITUD::${row.cveKdm1}::${row.neyemp}::${destinatario}`;
        case 'ENTREGADO':
            return `ENTREGADO::${row.cveKdm1}::${row.neyemp}`;
        default:
            return `UNKNOWN::${row.cveKdm1}::${row.neyemp}`;
    }
}

export const useDavsDetalle = () => {
    const [state, setState] = useState<State>({
        status: 'idle',
        open: false,
        kind: null,
        data: null,
        error: null,
        activeKey: null,
    });

    const acRef = useRef<AbortController | null>(null);
    const cacheRef = useRef(new Map<string, CacheValue>());
    const activeKeyRef = useRef<string | null>(null);

    useEffect(() => {
        return () => acRef.current?.abort();
    }, []);

    const closeDetalle = useCallback(() => {
        acRef.current?.abort();
        activeKeyRef.current = null;

        setState({
            status: 'idle',
            open: false,
            kind: null,
            data: null,
            error: null,
            activeKey: null,
        });
    }, []);

    const openDetalle = useCallback(async (row: DavsDetailRow) => {
        const kind = normalizeKind(row.tipo);

        if (!kind) {
            setState({
                status: 'error',
                open: true,
                kind: 'ASESORIA',
                data: null,
                error: 'El tipo de detalle DAVS no es válido.',
                activeKey: 'INVALID_KIND',
            });
            return null;
        }

        const key = makeDetailKey(row, kind);
        activeKeyRef.current = key;

        const hit = cacheRef.current.get(key);
        if (hit) {
            setState({
                status: 'success',
                open: true,
                kind: hit.kind,
                data: hit.data,
                error: null,
                activeKey: key,
            });
            return hit.data;
        }

        acRef.current?.abort();
        const ac = new AbortController();
        acRef.current = ac;

        setState((prev) => ({
            status: 'loading',
            open: true,
            kind,
            data: prev.activeKey === key ? prev.data : null,
            error: null,
            activeKey: key,
        }));

        try {
            let data: DavsDetailData = null;

            switch (kind) {
                case 'ASESORIA': {
                    const res: DavsAsesoriaResponse = await getDavsAsesoria(
                        {
                            cveKdm1: row.cveKdm1,
                            neyemp: row.neyemp,
                        },
                        { signal: ac.signal }
                    );
                    data = res;
                    break;
                }

                case 'TRAMITE': {
                    const res: DavsTramiteResponse = await getDavsTramite(
                        {
                            cveKdm1: row.cveKdm1,
                        },
                        { signal: ac.signal }
                    );
                    data = res;
                    break;
                }


                case 'SOLICITUD': {
                    const subtipo = row.tipo as 'PSE' | 'FRD' | 'FRB';

                    const res: DevsSolicitudResponse = await getDavsSolicitud(
                        {
                            cveKdm1: row.cveKdm1,
                            neyemp: row.neyemp,
                            destinatarioCheque: row.destinatarioCheque ?? ''
                        },
                        {
                            subtipo,
                            estatus: 'ENTREGADO',
                        },

                        { signal: ac.signal }
                    );

                    data = res;

                    break;
                }


                case 'ENTREGADO': {
                    const res: DavsEntregadoResponse = await getDavsEntregados(
                        {
                            cveKdm1: row.cveKdm1,
                        },
                        { signal: ac.signal }
                    );
                    data = res;

                    break;
                }

            }

            if (activeKeyRef.current !== key) return null;

            cacheRef.current.set(key, { kind, data });

            setState({
                status: 'success',
                open: true,
                kind,
                data,
                error: null,
                activeKey: key,
            });

            return data;
        } catch (err) {
            if (isAbortLike(err)) return null;
            if (activeKeyRef.current !== key) return null;

            const message =
                err instanceof Error && err.message
                    ? err.message
                    : 'No se pudo obtener el detalle DAVS.';

            setState({
                status: 'error',
                open: true,
                kind,
                data: null,
                error: message,
                activeKey: key,
            });

            return null;
        }
    }, []);

    const loading = state.status === 'loading';
    const ok = state.status === 'success';
    const idle = state.status === 'idle';

    return useMemo(() => {
        return {
            ...state,
            loading,
            ok,
            idle,
            openDetalle,
            closeDetalle,
        };
    }, [state, loading, ok, idle, openDetalle, closeDetalle]);
}