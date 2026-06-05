"use client";

import s from "./PensionesSkeleton.module.css";

export default function PensionesSkeleton() {
  return (
    <div className={s.wrap}>
      <div className={s.block} />
      <div className={s.grid}>
        <div className={s.card} />
        <div className={s.card} />
        <div className={s.card} />
      </div>
      <div className={s.table} />
    </div>
  );
}