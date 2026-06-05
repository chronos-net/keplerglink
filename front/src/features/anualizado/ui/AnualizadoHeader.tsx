"use client";

import { ApiEmpleado } from "../types/anualizado.dto";
import s from "./AnualizadoHeader.module.css";

type Props = {
  datos: ApiEmpleado;
};

export default function AnualizadoHeader({ datos }: Props) {
  const inicial = datos.nombre?.[0] ?? "U";

  return (
    <div className={s.wrapper}>
      <div className={s.left}>
        <div className="avatar">{inicial}</div>

        <div className={s.nameBlock}>
          <h2 className={s.title}>{datos.nombre}</h2>

          <div className={s.dataRow}>
            <div className={s.dataItem}>
              <span className={s.label}>CURP:</span>
              <span className={s.value}>{datos.curp ?? "—"}</span>
            </div>

            <div className={s.dataItem}>
              <span className={s.label}>RFC:</span>
              <span className={s.value}>{datos.rfc ?? "—"}</span>
            </div>

            <div className={s.dataItem}>
              <span className={s.label}>ISSEMYM:</span>
              <span className={s.value}>{datos.issemym ?? "—"}</span>
            </div>

            <div className={s.dataItem}>
              <span className={s.label}>CLAVE S.P. :</span>
              <span className={s.value}>{datos.id ?? "—"}</span>
            </div>
          </div>
        </div>
      </div>

      <div className={s.right} />
    </div>
  );
}