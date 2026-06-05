'use client';

import s from './prestamos-table.module.css';

type PrestamoRow = {
  id: string | number;
  fechaInicio: string;
  claveDeduccion: string;
  puesto: string;
  importeTotal: number;
  rentaQuincenal: number;
  saldo: number;
  plazos: number;
  qnasPorPagar: number;
  docRef: string;
};

type Props = {
  rows: PrestamoRow[];
};

function money(n?: number) {
  return (n ?? 0).toLocaleString('es-MX', {
    style: 'currency',
    currency: 'MXN',
    minimumFractionDigits: 2,
  }); 
}

export default function PrestamosTable({ rows }: Props) {
  return (
    <div className={s.card}>
      <div className={s.title}>Detalle de préstamos</div>

      <div className={s.tableWrapper}>
        <table className={s.table}>
          <thead>
            <tr>
              <th>Fecha inicio</th>
              <th>Clave deducción</th>
              <th>Puesto</th>
              <th>Importe total</th>
              <th>Renta quincenal</th>
              <th>Saldo</th>
              <th className={s.center}>Plazos</th>
              <th className={s.center}>Qnas. x pagar</th>
              <th className={s.center}>Doc. ref</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((row) => (
              <tr key={row.id}>
                <td>{row.fechaInicio}</td>
                <td className={s.center}>{row.claveDeduccion}</td>
                <td>{row.puesto}</td>
                <td className={s.amount}>{money(row.importeTotal)}</td>
                <td className={s.amount}>{money(row.rentaQuincenal)}</td>
                <td className={`${s.amount} ${s.saldo}`}>
                  {money(row.saldo)}
                </td>
                <td className={s.center}>{row.plazos}</td>
                <td className={s.center}>{row.qnasPorPagar}</td>
                <td className={s.center}>{row.docRef}</td>
              </tr>
            ))}

            {rows.length === 0 && (
              <tr>
                <td colSpan={9} className={s.emptyCell}>
                  No se encontraron préstamos para ese filtro.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
