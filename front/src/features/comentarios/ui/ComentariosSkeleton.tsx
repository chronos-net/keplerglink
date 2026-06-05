
"use client";

import s from "./ComentariosSkeleton.module.css";

export default function ComentariosSkeleton() {
  return (
    <div className={s.wrap}>
      {/* Header fake */}
      <div className={s.headerSkeleton}>
        <div className={s.avatar} />
        <div className={s.headerText}>
          <div className={s.lineLg} />
          <div className={s.lineSm} />
        </div>
      </div>

      {/* Cards fake */}
      <div className={s.cards}>
        {[1, 2, 3].map((i) => (
          <div key={i} className={s.card}>
            <div className={s.badge} />
            <div className={s.grid}>
              <div className={s.box} />
              <div className={s.box} />
              <div className={s.box} />
              <div className={s.box} />
            </div>
            <div className={s.comment} />
            <div className={s.footer} />
          </div>
        ))}
      </div>
    </div>
  );
}