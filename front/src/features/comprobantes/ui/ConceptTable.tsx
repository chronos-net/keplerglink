'use client';
import { ConceptRow } from '../types/comprobantes.types';
import s from './concept-table.module.css';

export default function ConceptTable({
  title,
  rows,
}: {
  title: string;
  rows: ConceptRow[];
}) {
  const money = (n: number) =>
    n.toLocaleString('es-MX', { style: 'currency', currency: 'MXN' });

  const isEmpty = rows.length === 0;
  const fewRows = rows.length > 0 && rows.length <= 3;

  return (
    <div className={s.card}>
      <div className={s.cardHeader}>
        <h3 className={s.title}>{title}</h3>
      </div>

      <div className={s.table} role="table" aria-label={title}>
        <div className={s.thead} role="row">
          <div className={s.th} role="columnheader">Clave</div>
          <div className={s.th} role="columnheader">Descripción</div>
          <div className={`${s.th} ${s.right}`} role="columnheader">Importe</div>
        </div>

        {/* 👇 EL TRUCO: clases dinámicas */}
        <div
          className={`${s.tbody} ${isEmpty ? s.tbodyEmpty : ''
            } ${fewRows ? s.tbodyFew : ''}`}
          role="rowgroup"
        >
          {rows.map((r, i) => {
            const isNeg = r.importe < 0;
            return (
              <div className={s.tr} role="row" key={`${r.clave}-${i}`}>
                <div className={`${s.td} ${s.mono}`} role="cell">
                  {r.clave}
                </div>

                <div
                  className={`${s.td} ${s.desc}`}
                  role="cell"
                  title={r.descripcion}
                >
                  {r.descripcion}
                </div>

                <div
                  className={`${s.td} ${s.mono} ${s.right} ${isNeg ? s.neg : s.pos
                    }`}
                  role="cell"
                >
                  {money(r.importe)}
                </div>
              </div>
            );
          })}

          {rows.length === 0 && (
            <div className={s.empty} role="row">
              <div className={s.emptyInner}>Sin conceptos</div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
