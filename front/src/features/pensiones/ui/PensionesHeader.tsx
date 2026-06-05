"use client";

import type { ServidorPublico } from "../types/pensiones.types";
import s from "./PensionesHeader.module.css";

const formatFecha = (fecha?: string) => {
  if (!fecha) return "-";

  const [dia, mes, anio] = fecha.split("-");

  const meses = [
    "Ene", "Feb", "Mar", "Abr", "May", "Jun",
    "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
  ];

  const diaFormateado = dia.padStart(2, "0");
  const mesNombre = meses[Number(mes) - 1] ?? mes;

  return `${diaFormateado} ${mesNombre} ${anio}`;
};

type Props = {
    servidor: ServidorPublico;
};

export default function PensionesHeader({ servidor }: Props) {
    const inicial = servidor.nombre?.trim()?.[0] ?? "U";

    return (
        <div className={s.wrap}>
            <div className={s.left}>
                <div className="avatar">{inicial}</div>

                <div className={s.block}>
                    <h2 className={s.name}>{servidor.nombre}</h2>

                    <div className={s.meta}>
                        <span className={s.item}><b>RFC:</b> {servidor.rfc}</span>
                        <span className={s.dot}>•</span>
                        <span className={s.item}><b>Clave S.P. :</b> {servidor.clavesp}</span>
                        <span className={s.dot}>•</span>
                        <span className={s.item}><b>Ingreso:</b> {formatFecha(servidor.fechaIngreso)}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}