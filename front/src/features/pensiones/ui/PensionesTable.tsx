"use client";

import type { ItemPostPension } from "../types/pensiones.types";
import s from "./PensionesTable.module.css";

type Props = {
  items: ItemPostPension[];
};

const money = (n: number) =>
  n.toLocaleString("es-MX", { style: "currency", currency: "MXN" });

export default function PensionesTable({ items }: Props) {
  return (
    <div className={s.wrap}>
      <div className={s.title}>Detalle de pensiones</div>

      <div className={s.tableWrap}>
        <table className={s.table}>
          <thead>
            <tr>
              <th>Beneficiario</th>
              <th>Tipo</th>
              <th>Alta</th>
              <th>%</th>
              <th>Importe</th>
              <th>Referencia</th>
            </tr>
          </thead>
          <tbody>
            {items.map((it, idx) => (
              <tr key={`${it.referencia}-${idx}`}>
                <td className={s.primary}>{it.beneficiario}</td>
                <td><span className={s.badge}>{it.tipo}</span></td>
                <td>{it.inicio}</td>
                <td>{it.porcentaje}%</td>
                <td className={s.money}>{money(it.importe)}</td>
                <td className={s.mono}>{it.referencia}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}