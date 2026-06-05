"use client";

import { PostCommentsHeader } from "../types/comentarios.post.types";
import s from "./ComentariosHeader.module.css";

type Props = {
    datos: PostCommentsHeader
}


export default function ComentariosHeader({ datos }: Props) {
    const inicial = datos.nombreCompleto?.[0] ?? "U";

    return (
        <div className={s.card}>
            <div className="avatar">{inicial}</div>
            <div className={s.nameBlock}>
                <h2 className={s.title}>{datos.nombreCompleto}</h2>

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
                        <span className={s.value}>{datos.imss ?? "—"}</span>
                    </div>
                    <div className={s.dataItem}>
                        <span className={s.label}>CLAVE DE S.P. :</span>
                        <span className={s.value}>{datos.neyemp ?? "—"}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
