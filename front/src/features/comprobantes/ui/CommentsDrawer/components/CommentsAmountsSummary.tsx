import { money } from '@/features/comprobantes/utils/commentsDrawer.utils';

import styles from './CommentsAmountsSummary.module.css';

type CommentsAmountsSummaryProps = {
    totalInicial: string | number | null | undefined;
    totalFinal: string | number | null | undefined;
    totalDiferencia: string | number | null | undefined;
};

export default function CommentsAmountsSummary({
    totalInicial,
    totalFinal,
    totalDiferencia,
}: CommentsAmountsSummaryProps) {
    return (
        <section className={styles.section}>
            <div className={styles.sectionHeader}>
                <p>Importe total de los periodos</p>
            </div>

            <div className={styles.kpiRow}>
                <div className={styles.kpi}>
                    <span>Inicial</span>
                    <strong>{money(totalInicial)}</strong>
                </div>

                <div className={styles.kpi}>
                    <span>Final</span>
                    <strong className={styles.mutedMoney}>
                        {money(totalFinal, true)}
                    </strong>
                </div>

                <div className={styles.kpi}>
                    <span>Diferencia</span>
                    <strong className={styles.negativeMoney}>
                        {money(totalDiferencia)}
                    </strong>
                </div>
            </div>
        </section>
    );
}
