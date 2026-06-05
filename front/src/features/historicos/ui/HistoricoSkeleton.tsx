// src/features/historicos/ui/HistoricoSkeleton.tsx
'use client';

import s from './HistoricoSkeleton.module.css';

function TableRows({ rows = 5 }: { rows?: number }) {
  return (
    <tbody>
      {Array.from({ length: rows }).map((_, index) => (
        <tr key={index}>
          <td><div className={`${s.shimmer} ${s.cellLg}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellMd}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellSm}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellMd}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellLg}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellMoney}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellXs}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellMd}`} /></td>
          <td><div className={`${s.shimmer} ${s.cellLg}`} /></td>
        </tr>
      ))}
    </tbody>
  );
}

export default function HistoricoSkeleton() {
  return (
    <div className={s.wrapper}>
      {/* Header card skeleton */}
      <section className={s.headerCard}>
        <div className={`${s.shimmer} ${s.avatar}`} />

        <div className={s.main}>
          <div className={`${s.shimmer} ${s.name}`} />

          <div className={s.metaRow}>
            <div className={`${s.shimmer} ${s.metaItem}`} />
            <div className={`${s.shimmer} ${s.metaItem}`} />
            <div className={`${s.shimmer} ${s.metaItem}`} />
            <div className={`${s.shimmer} ${s.metaItem}`} />
          </div>
        </div>

        <div className={s.totalBox}>
          <div className={`${s.shimmer} ${s.totalLabel}`} />
          <div className={`${s.shimmer} ${s.totalValue}`} />
        </div>
      </section>

      {/* Table skeleton */}
      <section className={s.tableCard}>
        <div className={`${s.shimmer} ${s.tableTitle}`} />

        <div className={s.tableScroll}>
          <table className={s.table}>
            <thead>
              <tr>
                <th><div className={`${s.shimmer} ${s.thLg}`} /></th>
                <th><div className={`${s.shimmer} ${s.thMd}`} /></th>
                <th><div className={`${s.shimmer} ${s.thSm}`} /></th>
                <th><div className={`${s.shimmer} ${s.thMd}`} /></th>
                <th><div className={`${s.shimmer} ${s.thLg}`} /></th>
                <th><div className={`${s.shimmer} ${s.thMoney}`} /></th>
                <th><div className={`${s.shimmer} ${s.thXs}`} /></th>
                <th><div className={`${s.shimmer} ${s.thMd}`} /></th>
                <th><div className={`${s.shimmer} ${s.thLg}`} /></th>
              </tr>
            </thead>

            <TableRows rows={6} />
          </table>
        </div>
      </section>
    </div>
  );
}