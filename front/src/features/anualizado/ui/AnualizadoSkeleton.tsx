// src/features/anualizado/ui/AnualizadoSkeleton.tsx
'use client';

import s from './AnualizadoSkeleton.module.css';

export default function AnualizadoSkeleton() {
  const rows = Array.from({ length: 10 });
  const cols = Array.from({ length: 12 });

  return (
    <div className={s.wrapper}>
      {/* Chips superiores */}
      <div className={s.header}>
        <div className={s.pill} style={{ width: 130 }} />
        <div className={s.pill} style={{ width: 90 }} />
        <div className={s.pill} style={{ width: 160 }} />
      </div>

      {/* Tabla fantasma */}
      <div className={s.table}>
        {/* Header */}
        <div className={`${s.row} ${s.headerRow}`}>
          <div className={`${s.cell} ${s.cellLeft}`} style={{ width: '100%' }} />
          {cols.map((_, i) => (
            <div key={`h-${i}`} className={s.cell} />
          ))}
        </div>

        {/* Body */}
        {rows.map((_, r) => (
          <div key={`r-${r}`} className={s.row}>
            <div
              className={`${s.cell} ${s.cellLeft}`}
              style={{ width: `${100 - (r % 4) * 10}%` }} // simula variaciones reales
            />
            {cols.map((_, c) => (
              <div
                key={`c-${r}-${c}`}
                className={s.cell}
                style={{
                  opacity: c % 3 === 0 ? 0.85 : 1, // ligeras variaciones estéticas
                }}
              />
            ))}
          </div>
        ))}

        {/* Totales */}
        <div className={`${s.row} ${s.footerRow}`}>
          <div className={`${s.cell} ${s.cellLeft}`} />
          {cols.map((_, i) => (
            <div key={`f-${i}`} className={`${s.cell} ${s.cellStrong}`} />
          ))}
        </div>
      </div>
    </div>
  );
}
