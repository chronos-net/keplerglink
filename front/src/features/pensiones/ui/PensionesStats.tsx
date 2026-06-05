"use client";

import type { StatsPensiones } from "../types/pensiones.types";
import s from "./PensionesStats.module.css";
import { ListChecks, Wallet, Percent } from "lucide-react";

type Props = {
  stats: StatsPensiones;
};

const money = (n: number) =>
  n.toLocaleString("es-MX", { style: "currency", currency: "MXN" });

export default function PensionesStats({ stats }: Props) {
  return (
    <div className={s.grid}>
      <div className={s.card}>
        <div className={s.row}>
          <div className={`${s.iconTile} ${s.t1}`}>
            <ListChecks size={16} />
          </div>
          <div className={s.text}>
            <div className={s.label}>Total de pensiones</div>
            <div className={s.value}>{stats.totalPensiones}</div>
          </div>
        </div>
      </div>

      <div className={s.card}>
        <div className={s.row}>
          <div className={`${s.iconTile} ${s.t2}`}>
            <Wallet size={16} />
          </div>
          <div className={s.text}>
            <div className={s.label}>Importe total</div>
            <div className={s.value}>{money(stats.importeTotal)}</div>
          </div>
        </div>
      </div>

      <div className={s.card}>
        <div className={s.row}>
          <div className={`${s.iconTile} ${s.t3}`}>
            <Percent size={16} />
          </div>
          <div className={s.text}>
            <div className={s.label}>Porcentaje promedio</div>
            <div className={s.value}>{stats.porcentajePromedio.toFixed(1)}%</div>
          </div>
        </div>
      </div>
    </div>
  );
}