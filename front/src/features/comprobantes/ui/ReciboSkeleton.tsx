'use client';
import s from './recibo-skeleton.module.css';

export default function ReciboSkeleton() {
  return (
    <div className={s.wrap} aria-hidden>
      {/* Header (avatar + meta + 3 columnas pequeñas) */}
      <div className={s.header}>
        <div className={s.avatar} />
        <div className={s.titleBlock}>
          <div className={s.lineLg} />
          <div className={s.lineSm} />
        </div>
        <div className={s.headerCols}>
          <div className={s.pill} />
          <div className={s.pill} />
          <div className={s.pill} />
        </div>
      </div>

      {/* Badges resumen */}
      <div className={s.badges}>
        <div className={s.badge} />
        <div className={s.badge} />
        <div className={s.badge} />
        <div className={s.badgeSm} />
        <div className={s.badgeSm} />
      </div>

      {/* Dos tablas */}
      <div className={s.columns}>
        <div className={s.table}>
          <div className={s.thead} />
          <div className={s.row} />
          <div className={s.row} />
          <div className={s.row} />
          <div className={s.row} />
        </div>
        <div className={s.table}>
          <div className={s.thead} />
          <div className={s.row} />
          <div className={s.row} />
          <div className={s.row} />
          <div className={s.row} />
        </div>
      </div>

      {/* Pie/neto */}
      <div className={s.footer}>
        <div className={s.footerCol} />
        <div className={s.footerCol} />
        <div className={s.footerCol} />
        <div className={s.footerTotal} />
      </div>
    </div>
  );
}
