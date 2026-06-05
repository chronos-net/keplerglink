import { CalendarDays, IdCard } from 'lucide-react';

import { pick } from '@/features/comprobantes/utils/commentsDrawer.utils';
import styles from './CommentsSummaryHero.module.css';

import type {
    GetComentariosCabecera,
    GetComentariosValor
} from '@/features/comentarios/types/comentarios.get.types';

type CommentsSummaryHeroProps = {
    header?: GetComentariosCabecera | null;
    firstItem?: GetComentariosValor | null;
    totalItems: number;
    curp?: string | null;
    periodo?: string;
    anio?: string;
};

export default function CommentsSummaryHero({
    header,
    firstItem,
    curp,
    periodo,
    anio,
}: CommentsSummaryHeroProps) {

    const periodoValue = periodo ?? firstItem?.qna;
    const anioValue = anio ?? firstItem?.anio;
    const initial = pick(header?.nombreCompleto).charAt(0).toUpperCase();

    return (
        <section className={styles.summaryHero}>
            <div className={styles.avatar}>{initial}</div>

            <div className={styles.heroContent}>
                <h4 className={styles.heroName}>
                    {pick(header?.nombreCompleto)}
                </h4>

                <div className={styles.heroMeta}>
                    <div className={styles.heroMetaItem}>
                        <IdCard size={14} />
                        <span>Clave:</span>
                        <strong>{pick(header?.neyemp)}</strong>
                    </div>

                    <div className={styles.heroMetaItem}>
                        <IdCard size={14} />
                        <span>Curp:</span>
                        <strong>{pick(curp)}</strong>
                    </div>

                    <div className={styles.heroMetaItem}>
                        <CalendarDays size={14} />
                        <span>Quincena:</span>
                        <strong>
                            {pick(periodoValue)} / {pick(anioValue)}
                        </strong>
                    </div>
                </div>
            </div>
        </section>
    );
}
