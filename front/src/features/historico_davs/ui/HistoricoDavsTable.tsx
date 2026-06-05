'use client'

import { Eye, Search } from 'lucide-react';
import { HistoricoDavsCounts, HistoricoDavsTab, HistoricoDavsTableRow } from '../types/historicoDavs.types';
import { formatDateISO, TAB_FILTERS, tipoLabel } from '../utils/formateDateIso';
import { useDavsDetalle } from '../hooks/useDavsDetalle';
import DavsDetailModal from './DavsDetailModal';
import s from './HistoricoDavsTable.module.css';

type Props = {
    rows: HistoricoDavsTableRow[];
    tab: HistoricoDavsTab;
    onTabChange: (t: HistoricoDavsTab) => void;
    folioQ: string;
    onFolioChange: (v: string) => void;
    counts: HistoricoDavsCounts;
    onViewDetail?: (row: HistoricoDavsTableRow) => void;
    onPrint?: (row: HistoricoDavsTableRow) => void;
}

export default function HistoricoDavsTable({
    rows,
    tab,
    onTabChange,
    folioQ,
    onFolioChange,
    counts
}: Props) {

    const total = counts.todos ?? 0;

    const {
        open,
        loading,
        error,
        data,
        kind,
        openDetalle,
        closeDetalle,
    } = useDavsDetalle();

    return (
        <div className={s.card}>
            <div className={s.header}>
                <div className={s.title}>Historial</div>

                <div className={s.badge}>
                    {total} registro{total !== 1 ? 's' : ''} encontrado{total !== 1 ? 's' : ''}
                </div>
            </div>

            <div className={s.controls}>
                <div className={s.searchWrap}>
                    <Search size={14} className={s.searchIcon} />
                    <input
                        value={folioQ}
                        onChange={(e) => onFolioChange(e.target.value)}
                        placeholder="Buscar por folio..."
                        className={s.search}
                    />
                </div>

                <div className={s.tabs}>
                    {TAB_FILTERS.map((data) => (
                        <button
                            key={data.value}
                            type="button"
                            className={`${s.tab} ${tab === data.value ? s.tabActive : ''}`}
                            onClick={() => onTabChange(data.value)}
                        >
                            <span>{data.label}</span>
                            <span className={s.tabCount}>{counts[data.value] ?? 0}</span>
                        </button>
                    ))}
                </div>
            </div>

            {rows.length === 0 ? (
                <div className={s.empty}>
                    <div className={s.emptyTitle}>Sin registros</div>
                    <div className={s.emptyText}>
                        No se encontraron registros con los filtros seleccionados.
                    </div>
                </div>
            ) : (
                <div className={s.tableWrap}>
                    <table className={s.table}>
                        <thead>
                            <tr>
                                <th className={s.colFolio}>Folio Documento</th>
                                <th className={s.colFecha}>Fecha</th>
                                <th> Destinatario / Tipo mov.</th>
                                <th className={s.colTipo}>Tipo</th>
                                <th className={s.colAcciones}>Acciones</th>
                            </tr>

                        </thead>


                        <tbody>
                            {rows.map((r, idx) => (
                                <tr key={`${r.tipo}-${r.cveKdm1}-${idx}`}>

                                    <td className={s.mono}>{r.folioDocumento}</td>
                                    <td className={s.muted}>{formatDateISO(r.fecha)}</td>
                                    <td>
                                        {r.destinatarioCheque ? (
                                            r.destinatarioCheque
                                        ) : (
                                            <span className={s.muted}>&mdash;</span>
                                        )}
                                    </td>
                                    <td>
                                        <span className={`${s.tipoBadge} ${s['tipo_' + r.tipo]}`}>
                                            {tipoLabel(r.tipo)}
                                        </span>
                                    </td>

                                    <td className={s.actions}>
                                        <button
                                            type="button"
                                            className={s.viewBtn}
                                            onClick={() =>
                                                openDetalle({
                                                    tipo: r.tipo,
                                                    cveKdm1: r.cveKdm1,
                                                    neyemp: r.neyemp,
                                                    destinatarioCheque: r.destinatarioCheque ?? null,
                                                })
                                            }
                                            aria-label="Ver detalle"
                                            title="Ver detalle"
                                        >
                                            <Eye size={14} />
                                            <span>Ver</span>
                                        </button>
                                    </td>
                                </tr>
                            ))}

                        </tbody>
                    </table>
                </div>
            )}



            <DavsDetailModal
                open={open}
                loading={loading}
                error={error}
                kind={kind}
                data={data}
                onClose={closeDetalle}
            />
        </div>
    )
}
