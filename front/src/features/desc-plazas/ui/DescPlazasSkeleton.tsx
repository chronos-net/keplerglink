// src/features/desc-plazas/ui/DescPlazasSkeleton.tsx
'use client'

import s from './DescPlazasSkeleton.module.css'

function SkeletonRows({ rows = 4 }: { rows?: number }) {
  return (
    <div className={s.table}>
      {Array.from({ length: rows }).map((_, idx) => (
        <div key={idx} className={s.tr}>
          <div className={`${s.shimmer} ${s.tdCode}`} />
          <div className={`${s.shimmer} ${s.tdAmt}`} />
        </div>
      ))}
    </div>
  )
}

function SkeletonBox() {
  return (
    <section className={s.box}>
      <div className={s.boxTop}>
        <div className={`${s.shimmer} ${s.boxTitle}`} />
        <div className={`${s.shimmer} ${s.boxTotal}`} />
      </div>

      <SkeletonRows rows={5} />
    </section>
  )
}

function SkeletonCard() {
  return (
    <article className={s.card}>
      <div className={s.cardHead}>
        <div className={s.cardLeft}>
          <div className={s.cardTitle}>
            <div className={`${s.shimmer} ${s.badge}`} />
            <div className={`${s.shimmer} ${s.mono}`} />
          </div>

          <div className={`${s.shimmer} ${s.meta}`} />
        </div>

        <div className={s.cardRight}>
          <div className={`${s.shimmer} ${s.metaMini}`} />
          <div className={`${s.shimmer} ${s.metaMini}`} />
        </div>
      </div>

      <div className={s.cols}>
        <SkeletonBox />
        <SkeletonBox />
      </div>
    </article>
  )
}

export default function DescPlazasSkeleton() {
  return (
    <section className={s.panel}>
      <div className={s.header}>
        <div>
          <div className={`${s.shimmer} ${s.title}`} />
          <div className={`${s.shimmer} ${s.sub}`} />
        </div>

        <div className={s.kpiRow}>
          {Array.from({ length: 3 }).map((_, idx) => (
            <div key={idx} className={s.kpi}>
              <div className={`${s.shimmer} ${s.kpiLabel}`} />
              <div className={`${s.shimmer} ${s.kpiValue}`} />
            </div>
          ))}
        </div>
      </div>

      <div className={s.cards}>
        <SkeletonCard />
        <SkeletonCard />
      </div>
    </section>
  )
}