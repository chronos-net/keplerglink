// src/features/historicos/ui/HistoricoTable.tsx
'use client';


import { HistoricoDetalle } from '../types/historico.types';
import { formatCurrencyMXN } from '../utils/format';
import s from './HistoricoTable.module.css';

type Props = {
  rows: HistoricoDetalle[];
};

export default function HistoricoTable({ rows }: Props) {
  if (!rows.length) return null;

  return (
    <div className={s.card}>
      <div className={s.header}>Detalle de histórico</div>

      <div className={s.tableScroll}>
        <table className={s.table}>
          <thead>
            <tr>
              <th>Dependencia</th>
              <th>U. responsable</th>
              <th>Plaza</th>
              <th>Categoría</th>
              <th>Periodo ocupación</th>
              <th className={s.numeric}>Total percepciones</th>
              <th>Horas</th>
              <th>Tipo de plaza</th>
              <th>Motivo</th>
            </tr>
          </thead>

          <tbody>
            {rows.map((row, index) => (
              <tr
                key={`${row.dependencia}-${row.unidadResponsable}-${row.plaza}-${row.categoria}-${index}`}
                className={index % 2 === 0 ? s.rowEven : s.rowOdd}
              >
                <td>{row.dependencia}</td>
                <td>{row.unidadResponsable}</td>
                <td>{row.plaza}</td>
                <td>{row.categoria}</td>
                <td>{row.periodoOcupacion}</td>
                <td className={s.numeric}>{formatCurrencyMXN(row.totalPercepciones)}</td>
                <td>{row.horas}</td>
                <td>{row.tipoPlaza}</td>
                <td>{row.motivo}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}