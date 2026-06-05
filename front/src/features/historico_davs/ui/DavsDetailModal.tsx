'use client';

import BaseModal from '@/components/modals/BaseModal';
import { DavsDetailKind } from '../types/historicoDavs.types';
import { getDavsDetailTitle } from '../const/davsDetail';

import DavsDetailLoading from './detail-states/DavsDetailLoading';
import DavsDetailError from './detail-states/DavsDetailError';
import DavsDetailEmpty from './detail-states/DavsDetailEmpty';
import DavsAsesoriaBody from './detail-bodies/DavsAsesoriaBody';
import { DavsAsesoriaResponse } from '../types/historicoAsesoria.get.types';
import DavsEntregadoBody from './detail-bodies/DavsEntregadoBody';
import { DavsEntregadoResponse } from '../types/historicoEntregados.types';
import DavsTramiteBody from './detail-bodies/DavsTramiteBody';
import { DavsTramiteResponse } from '../types/historicoTramite.get.types';
import DavsSolicitudBody from './detail-bodies/DavsSolicitudBody';
import { DevsSolicitudResponse } from '../types/historicoSolicitud.types';

type Props = {
    open: boolean;
    loading: boolean;
    error: string | null;
    kind: DavsDetailKind | null;
    data: unknown;
    onClose: () => void;
};

export default function DavsDetailModal({
    open,
    loading,
    error,
    kind,
    data,
    onClose
}: Props) {
    
    const title = getDavsDetailTitle(kind);

    return (

        <BaseModal open={open} onClose={onClose} title={title} width="md">
            {loading ? (
                <DavsDetailLoading />
            ) : error ? (
                <DavsDetailError message={error} />
            ) : !data ? (
                <DavsDetailEmpty />
            ) : kind === 'ASESORIA' ? (
                <DavsAsesoriaBody data={data as DavsAsesoriaResponse} />
            ) : kind === 'ENTREGADO' ? (
                <DavsEntregadoBody data={data as DavsEntregadoResponse} />
            ) : kind === 'TRAMITE' ? (
                <DavsTramiteBody data={data as DavsTramiteResponse} />
            ) : kind === 'SOLICITUD' ? (
                <DavsSolicitudBody data={data as DevsSolicitudResponse} />
            ) : (
                <DavsDetailEmpty />
            )}
        </BaseModal>
    );
}