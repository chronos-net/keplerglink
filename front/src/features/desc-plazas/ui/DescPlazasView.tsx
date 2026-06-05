'use client'

import s from './DescPlazasView.module.css'
import type { DescPlazasResponseDto } from '../types/desc-plazas.dto'
import { FileDown } from 'lucide-react'

export type DescPlazasViewProps = {
  data: DescPlazasResponseDto
  anio: number
  quincena: string
  onGeneratePdf: () => void | Promise<void>
  pdfBusy?: boolean
}

const money = (n?: number | string | null) =>
  (n ?? 0).toLocaleString('es-MX', { style: 'currency', currency: 'MXN' })

const dash = (v?: string | number | null) =>
  v === undefined || v === null || v === '' ? '—' : String(v)

function Meta({ label, value }: { label: string; value: string }) {
  return (
    <div className={s.meta}>
      <span className={s.metaLabel}>{label}</span>
      <span className={s.metaValue}>{value}</span>
    </div>
  )
}

function TotalItem({
  label,
  value,
  highlight,
}: {
  label: string
  value: string
  highlight?: boolean
}) {
  return (
    <div className={`${s.totalItem} ${highlight ? s.totalHighlight : ''}`}>
      <span className={s.totalLabel}>{label}</span>
      <span className={s.totalValue}>{value}</span>
    </div>
  )
}

export default function DescPlazasView({
  data,
  anio,
  quincena,
  onGeneratePdf,
  pdfBusy = false,
}: DescPlazasViewProps) {
  const empleado = data.empleado
  const plazaPrincipal = data.plazaPrincipal
  const plazas = data.plazas ?? []
  const hasPlazas = plazas.length > 0
  const inicial = (empleado.negnom?.[0] ?? 'U').toUpperCase()

  const puestoLabel = (puesto?: string, leyenda?: string) => {
    const p = dash(puesto)
    const l = leyenda?.trim()
    if (p === '—' && !l) return '—'
    if (!l || l === '—') return p
    if (p === '—') return l
    return `${p} · ${l}`
  }

  return (
    <section className={s.wrap}>
      <div className={s.card}>
        <div className={s.reportToolbar}>
          <div className={s.reportTitleBlock}>
            <span className={s.reportTitle}>Desglose de plazas</span>
            <span className={s.periodBadge}>
              Periodo {quincena} &mdash; {anio}
            </span>
          </div>
          <button
            type="button"
            className={s.pdfBtn}
            disabled={pdfBusy}
            onClick={() => void onGeneratePdf()}
          >
            <FileDown size={16} aria-hidden />
            <span>{pdfBusy ? 'Generando…' : 'Generar PDF'}</span>
          </button>
        </div>

        <header className={s.headerGrid}>
          <div className={s.personCol}>
            <div className={s.personRow}>
              <div className={s.avatar}>{inicial}</div>
              <div className={s.personMeta}>
                <div className={s.personName}>{dash(empleado.negnom)}</div>
                <div className={s.personIds}>
                  <span>Clave {dash(empleado.neyemp)}</span>
                  <span className={s.dot} aria-hidden />
                  <span>RFC {dash(empleado.rfc)}</span>
                </div>
              </div>
            </div>
          </div>

          <div className={s.midCol}>
            <div className={s.sectionTitle}>Datos de pago y adscripción</div>
            <div className={s.metaGrid}>
              <Meta label="Adscripción" value={dash(empleado.ads)} />
              <Meta label="Cheque" value={dash(empleado.cheque)} />
              <Meta label="Banco" value={dash(empleado.banco)} />
              <Meta label="Cuenta" value={dash(empleado.numCuenta)} />
              <Meta label="Recibo" value={dash(empleado.numRecibo)} />
            </div>
          </div>

          <div className={s.rightCol}>
            <div className={s.sectionTitle}>Plaza principal</div>
            <div className={s.metaGrid}>
              <Meta label="Plaza" value={dash(plazaPrincipal?.plazaId)} />
              <Meta label="Secuencia" value={dash(plazaPrincipal?.secuenciaPlaza)} />
            </div>
          </div>
        </header>

        <div className={s.totalsBar}>
          <TotalItem
            label="Percepciones"
            value={money(data.totalGlobales.percepciones)}
          />
          <TotalItem
            label="Deducciones"
            value={money(data.totalGlobales.deducciones)}
          />
          <TotalItem
            label="Neto global"
            value={money(data.totalGlobales.neto)}
            highlight
          />
        </div>
      </div>

      {hasPlazas ? (
        <div className={s.plazas}>
          {plazas.map((pz, i) => (
            <article
              key={`${pz.plazaId}-${pz.secuenciaPlaza}-${i}`}
              className={s.plazaSection}
            >
              <div className={s.plazaHead}>
                <span className={s.plazaBadge}>Plaza {i + 1}</span>
                <Meta label="Plaza" value={dash(pz.plazaId)} />
                <Meta label="Sec." value={dash(pz.secuenciaPlaza)} />
                <Meta label="Puesto" value={puestoLabel(pz.puesto, pz.leyendaPuesto)} />
                <Meta label="Lug. pago" value={dash(pz.lugpago)} />
                <Meta label="Centro" value={dash(pz.centroTrabajo)} />
              </div>

              <div className={s.cols}>
                <section className={s.conceptBox}>
                  <div className={s.conceptHead}>Percepciones</div>
                  <div className={s.tableScroll}>
                    {pz.percepciones?.percepciones?.length ? (
                      <div className={s.table}>
                        <div className={s.thead}>
                          <span>Clave</span>
                          <span className={s.right}>Importe</span>
                        </div>
                        {pz.percepciones.percepciones.map((it, idx) => (
                          <div key={idx} className={s.tr}>
                            <span className={s.tdCode}>{dash(it.codigo)}</span>
                            <span className={s.tdAmt}>{money(it.importe)}</span>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <div className={s.empty}>Sin percepciones.</div>
                    )}
                  </div>
                </section>

                <section className={s.conceptBox}>
                  <div className={s.conceptHead}>Deducciones</div>
                  <div className={s.tableScroll}>
                    {pz.deducciones?.deducciones?.length ? (
                      <div className={s.table}>
                        <div className={s.thead}>
                          <span>Clave</span>
                          <span className={s.right}>Importe</span>
                        </div>
                        {pz.deducciones.deducciones.map((it, idx) => (
                          <div key={idx} className={s.tr}>
                            <span className={s.tdCode}>{dash(it.codigo)}</span>
                            <span className={s.tdAmt}>{money(it.importe)}</span>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <div className={s.empty}>Sin deducciones.</div>
                    )}
                  </div>
                </section>
              </div>

              <div className={s.plazaTotalsBar}>
                <TotalItem
                  label="Total percepciones"
                  value={money(pz.percepciones?.total)}
                />
                <TotalItem
                  label="Total deducciones"
                  value={money(pz.deducciones?.total)}
                />
                <TotalItem label="Neto" value={money(pz.neto)} highlight />
              </div>
            </article>
          ))}
        </div>
      ) : (
        <div className={s.emptyState}>
          <div className={s.emptyTitle}>Sin detalle de plazas</div>
          <div className={s.emptyText}>
            Se encontró información general del servidor público, pero no hay desglose
            detallado de plazas disponible para esta consulta.
          </div>
        </div>
      )}
    </section>
  )
}
