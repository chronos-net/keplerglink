'use client';

import s from './HistoricoDavsSkeleton.module.css';

export default function HistoricoDavsSkeleton() {
  return (
    <div className={s.wrap}>
      <div className={s.headerRow}>
        <div className={s.hTitle} />
        <div className={s.hPill} />
      </div>

      <div className={s.tabsRow}>
        <div className={s.tab} />
        <div className={s.tab} />
        <div className={s.tab} />
        <div className={s.tab} />
        <div className={s.tab} />
      </div>

      <div className={s.searchRow}>
        <div className={s.search} />
      </div>

      <div className={s.table}>
        <div className={s.tHead}>
          <div className={s.th} />
          <div className={s.th} />
          <div className={s.th} />
          <div className={s.th} />
          <div className={s.th} />
        </div>

        {Array.from({ length: 6 }).map((_, i) => (
          <div key={i} className={s.tRow}>
            <div className={s.tdBadge} />
            <div className={s.td} />
            <div className={s.td} />
            <div className={s.tdWide} />
            <div className={s.tdBtn} />
          </div>
        ))}
      </div>
    </div>
  );
}