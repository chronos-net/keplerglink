import {
    Building2,
    CheckCircle2,
    ChevronDown,
    FileText,
    MapPin,
    MessageSquareText,
} from 'lucide-react';

import type {
    GetComentariosCabecera,
    GetComentariosValor,
} from '@/features/comentarios/types/comentarios.get.types';

import {
    formatDateShort,
    money,
    pick,
    pickAccount,
} from '@/features/comprobantes/utils/commentsDrawer.utils';

import styles from './CommentRecordCard.module.css';

type CommentRecordCardProps = {
    item: GetComentariosValor;
    header?: GetComentariosCabecera | null;
    recordKey: string;
    hasComment: boolean;
    commentOpen: boolean;
    onToggleComment: (recordKey: string) => void;
};

type CatalogValue = GetComentariosValor['adscripcion'];


const AmountBox = ({
    label,
    value,
    tone = 'default',
}: {
    label: string;
    value: number | string | null | undefined;
    tone?: 'default' | 'danger' | 'success';
}) => {
    return (
        <div className={`${styles.amountBox} ${styles[`amountBox_${tone}`]}`}>
            <span>{label}</span>
            <strong>{money(value, label === 'Final')}</strong>
        </div>
    );
}

const InfoItem = ({
    label,
    value,
}: {
    label: string;
    value: string;
}) => {
    return (
        <div className={styles.infoItem}>
            <span>{label}</span>
            <strong>{value}</strong>
        </div>
    );
}

const CatalogRow = ({
    icon,
    label,
    value,
}: {
    icon: React.ReactNode;
    label: string;
    value: CatalogValue;
}) => {
    return (
        <div className={styles.catalogRow}>
            <div className={styles.catalogIcon}>{icon}</div>
            <div className={styles.catalogContent}>
                <span>{label}</span>
                <div className={styles.catalogValue}>
                    <strong>{pick(value?.clave)}</strong>
                    <p>{pick(value?.descripcion)}</p>
                </div>
            </div>
        </div>
    );
}

export default function CommentRecordCard({
    item,
    hasComment,
}: CommentRecordCardProps) {
    const capturado = pick(item.capturado);
    const hasCapturado = capturado !== '-';
    const fechaCaptura = formatDateShort(item.fechaCaptura);
    const differenceTone = Number(item.diferencia) < 0 ? 'danger' : 'success';

    return (
        <li className={styles.recordCard}>
            <div className={styles.recordTop}>
                <div className={styles.movementPill}>
                    <span>Movimiento:</span>
                    <strong>{pick(item.movimiento)}</strong>
                </div>

                <div
                    className={`${styles.statusPill} ${hasComment ? styles.statusPill_ok : styles.statusPill_empty
                        }`}
                >
                    {hasComment ? (
                        <>
                            <span>Con Comentario</span>
                            <CheckCircle2 size={18} />
                        </>
                    ) : (
                        <>
                            <span>Sin Comentario</span>
                            <MessageSquareText size={17} />
                        </>
                    )}
                </div>
            </div>

            <div className={styles.infoGrid}>
                <InfoItem label="Forma de Pago" value={pick(item.formaPago)} />
                <InfoItem label="Cuenta / Cheque" value={pickAccount(item.numCuenta)} />
                <InfoItem label="Fecha Captura" value={fechaCaptura} />
            </div>

            <div className={styles.amountGrid}>
                <AmountBox label="Inicial" value={item.importeInicial} />
                <AmountBox label="Final" value={item.importeFinal} />
                <AmountBox
                    label="Diferencia"
                    value={item.diferencia}
                    tone={differenceTone}
                />
            </div>

            <div className={styles.commentArea}>
                {hasComment ? (
                    <div className={styles.commentCard}>
                        <div className={styles.commentFooter}>
                            {hasCapturado ? (
                                <span>
                                    Capturo: <strong>{capturado}</strong>
                                </span>
                            ) : (
                                <span>Comentario registrado</span>
                            )}
                        </div>

                        <p className={styles.commentText}>{pick(item.comentario)}</p>
                    </div>
                ) : (
                    <div className={styles.emptyComment}>
                        <MessageSquareText size={15} />
                        Sin comentario registrado
                    </div>
                )}
            </div>

            <details className={styles.adminDetails}>
                <summary className={styles.adminSummary}>
                    <div className={styles.adminSummaryTitle}>
                        <FileText size={19} />
                        <span>Datos administrativos</span>
                    </div>

                    <span className={styles.adminChevron}>
                        <ChevronDown size={18} />
                    </span>
                </summary>

                <div className={styles.adminBody}>
                    <CatalogRow
                        icon={<Building2 size={16} />}
                        label="Adscripcion"
                        value={item.adscripcion}
                    />
                    <CatalogRow
                        icon={<FileText size={16} />}
                        label="Puesto"
                        value={item.puesto}
                    />
                    <CatalogRow
                        icon={<MapPin size={16} />}
                        label="Lugar de pago"
                        value={item.lugarPago}
                    />
                </div>
            </details>
        </li>
    );
}
