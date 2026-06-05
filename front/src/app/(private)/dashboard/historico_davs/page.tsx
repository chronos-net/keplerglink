'use client';

import { useCallback, useState } from 'react';

import { useHistoricoDavs } from '@/features/historico_davs/hooks/useHistoricoDavs';
import type { HistoricoDavsQuery } from '@/features/historico_davs/types/historicoDavs.types';

import UniversalFilter from '@/components/filters/UniversalFilter';
import EmptyState from '@/features/comprobantes/ui/EmptyState';

import s from './page.module.css';
import HistoricoDavsSkeleton from '@/features/historico_davs/ui/HistoricoDavsSkeleton';
import HistoricoDavsTable from '@/features/historico_davs/ui/HistoricoDavsTable';

export default function PageHistoricoDavs() {
    const {
        consultar,
        loading,
        error,
        rows,
        tab,
        setTab,
        folioQ,
        setFolioQ,
        counts,
        hasData
    } = useHistoricoDavs();

    const [touched, setTouched] = useState(false);

    const onFilter = useCallback(
        async (q: { text?: string; neyempResolved?: string }) => {
            setTouched(true);

            const raw = (q.text ?? '').trim();
            if (!raw && !q.neyempResolved) return;

            let payload: HistoricoDavsQuery;

            if (q.neyempResolved) {
                payload = { neyemp: q.neyempResolved };
            } else if (/^\d+$/.test(raw)) {
                payload = { neyemp: raw };
            } else {
                payload = { negnom: raw };
            }

            await consultar(payload);
        },
        [consultar]
    );

    // flags (deja esto arriba)
    const showInitial = !touched && !hasData && !loading && !error;
    const showError = !!error && !loading;
    const showEmpty = touched && hasData && counts.todos === 0 && !loading && !error;
    const showTable = counts.todos > 0 && !loading && !error;

    return (
        <section className={s.page}>
            <h1 className={s.title}>Prestaciones Socioeconomicas</h1>

            <div className={s.filterRow}>
                <UniversalFilter
                    busy={loading}
                    initialText=""
                    textPlaceholder="Nombre o Clave de servidor"
                    onFilter={onFilter}
                    autocompleteSource="DAVS_HISTORICO"
                    modoEnvioTexto="resueltoONumerico"
                    showYear={false}
                    showQuincena={false}
                    requireText
                />
            </div>

            {loading && <HistoricoDavsSkeleton />}

            {showTable && (
                <HistoricoDavsTable
                    rows={rows}
                    tab={tab}
                    onTabChange={setTab}
                    folioQ={folioQ}
                    onFolioChange={setFolioQ}
                    counts={counts}
                />
            )}

            {showError && (
                <EmptyState
                    imgSrc="/img/error.png"
                    title="No pudimos consultar tu histórico DAVS"
                    description={error ?? 'Ocurrió un error inesperado.'}
                />
            )}

            {showEmpty && (
                <EmptyState
                    imgSrc="/img/error.png"
                    title="Sin resultados"
                    description="No encontramos movimientos para ese servidor. Verifica la clave/nombre e intenta de nuevo."
                />
            )}

            {showInitial && (
                <EmptyState
                    imgSrc="/img/buscar_recibo.png"
                    title="Consulta el histórico DAVS"
                    description="Ingresa el nombre o la clave del servidor público para consultar su historial."
                />
            )}
        </section>
    );
}