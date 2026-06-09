// src/features/historicos/ui/HistoricoHeaderCard.tsx
'use client';

import { HistoricoHeader } from '../types/historico.types';
import { formatCurrencyMXN } from '../utils/format';
import s from './HistoricoHeaderCard.module.css';

type Props = {
  header: HistoricoHeader;
  totalPercepciones: number;
};

export default function HistoricoHeaderCard({ header, totalPercepciones }: Props) {
  const initial =
    (header.nombre || header.neyemp || '?').trim().charAt(0).toUpperCase() || '?';

  const compactItems = [
    ['Situación', header.situacion],
    ['Categoría', header.categoria],
    ['Dependencia', header.dep],
    ['Unidad resp.', header.uresp],
    ['Plaza', header.plaza],
    ['CCT', header.cct],
    ['Horas', header.horas],
    ['Puesto', header.puesto],
    ['Formato Periodo', header.perdeocupacionFormato]
  ] as const;

  return (
    <section className={s.card}>
      <div className={s.hero}>
        <div className={s.heroLeft}>
          <div className={s.avatar}>{initial}</div>

          <div className={s.identity}>
            <span className={s.eyebrow}>Histórico laboral</span>
            <h2 className={s.name}>{header.nombre || '-'}</h2>

            <div className={s.metaInline}>
              <span className={s.metaChip}>
                <span className={s.metaChipLabel}>Clave S.P.</span>
                <strong>{header.neyemp || '-'}</strong>
              </span>

              <span className={s.metaChip}>
                <span className={s.metaChipLabel}>RFC</span>
                <strong>{header.rfc || '-'}</strong>
              </span>

              <span className={s.metaChip}>
                <span className={s.metaChipLabel}>CURP</span>
                <strong>{header.curp || '-'}</strong>
              </span>
            </div>
          </div>
        </div>

        <div className={s.totalBox}>
          <span className={s.totalLabel}>Total percepciones histórico</span>
          <strong className={s.totalValue}>
            {formatCurrencyMXN(totalPercepciones)}
          </strong>
        </div>
      </div>

      <div className={s.compactStrip}>
        <div className={s.compactRail}>
          {compactItems.map(([label, value]) => (
            <div
              className={`${s.compactItem} ${
                label === 'Formato Periodo' ? s.compactItemPeriod : ''
              }`}
              key={label}
            >
              <span className={s.compactLabel}>{label}</span>
              <strong
                className={`${s.compactValue} ${
                  label === 'Formato Periodo' ? s.compactValuePeriod : ''
                }`}
                title={value || undefined}
              >
                {value || '-'}
              </strong>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
