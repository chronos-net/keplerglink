'use client';

import { X, CalendarDays, Landmark } from 'lucide-react';
import s from './pensiones-drawer.module.css';
import { PensionCabeceraSimp, PensionItem } from '@/features/pensiones/types/pensiones.types';

type Props = {
  open: boolean;
  onClose: () => void;
  loading: boolean;
  error: string | null;
  header?: PensionCabeceraSimp | null;
  items: PensionItem[];
};

const pick = (v?: string | null) => (v && String(v).trim().length ? String(v) : '—');

export default function PensionesDrawer({
  open,
  onClose,
  loading,
  error,
  header,
  items,
}: Props) {
  if (!open) return null;


  const money = (v?: string | null) => {
    const n = Number(v ?? 0);
    if (Number.isNaN(n)) return pick(v);
    return n.toLocaleString('es-MX', { style: 'currency', currency: 'MXN' });
  };

  const showEmpty = !loading && !error && items.length === 0;

  return (
    <div className={s.backdrop} role="dialog" aria-modal="true">
      <aside className={s.sheet} onClick={(e) => e.stopPropagation()}>
        {/* HEADER */}
        <header className={s.header}>
          <div className={s.headerLeft}>
            <div className={s.headerText}>
              <h3 className={s.title}>Detalles Pensiones</h3>
              <p className={s.subtitle}>Detalle de percepciones, deducciones y beneficiarios</p>
            </div>
          </div>

          <button className={s.closeBtn} type="button" onClick={onClose} aria-label="Cerrar">
            <X size={18} />
          </button>
        </header>

        {/* BODY */}
        <div className={s.body}>
          {/* LOADING */}
          {loading && <div className={s.state}>Cargando pensiones…</div>}

          {/* ERROR */}
          {!loading && error && <div className={s.error}>{error}</div>}

          {/* CONTENT */}
          {!loading && !error && (
            <>
              {/* RESUMEN */}
              <section className={s.section}>
                <div className={s.sectionTitle}>RESUMEN</div>

                {/* Barra roja (full width) */}
                <div className={s.qnaBar}>
                  <CalendarDays size={18} />
                  <div className={s.qnaBarText}>
                    <div className={s.qnaMain}>Periodo {pick(header?.periodo)} / Año {pick(header?.anio)}</div>
                    <div className={s.qnaSub}>Clave de Servidor Público: {pick(header?.neyemp)}</div>
                  </div>
                </div>

                <div className={s.summaryCard}>
                  <div className={s.summaryGrid}>
                    <div>
                      <div className={s.label}>Adscripción</div>
                      <div className={s.value}>
                        {pick(header?.adsc)}{header?.leyenda_adscripcion ? ` - ${header.leyenda_adscripcion}` : ''}
                      </div>
                    </div>

                    <div>
                      <div className={s.label}>Puesto</div>
                      <div className={s.value}>
                        {pick(header?.puesto)}{header?.leyenda_puesto ? ` - ${header.leyenda_puesto}` : ''}
                      </div>
                    </div>

                    <div>
                      <div className={s.label}>Lugar de pago</div>
                      <div className={s.valueMono}>{pick(header?.lug_pago)}</div>
                    </div>

                    <div>
                      <div className={s.label}>Cuenta/Cheque</div>
                      <div className={s.valueMono}>
                        {pick(header?.num_cuenta)}{header?.cheque ? ` / ${header.cheque}` : ''}
                      </div>
                    </div>
                  </div>
                </div>
              </section>

              {/* MONTOS */}
              <section className={s.section}>
                <div className={s.sectionTitle}>MONTOS</div>

                <div className={s.kpiGrid}>
                  <div className={s.kpi}>
                    <div className={s.kpiLabel}>Percepciones</div>
                    <div className={s.kpiValue}>{money(header?.percep)}</div>
                  </div>

                  <div className={s.kpi}>
                    <div className={s.kpiLabel}>Deducciones</div>
                    <div className={s.kpiValue}>{money(header?.ded)}</div>
                  </div>

                  <div className={s.kpi}>
                    <div className={s.kpiLabel}>Neto</div>
                    <div className={s.kpiValue}>{money(header?.neto)}</div>
                  </div>
                </div>
              </section>

              {/* PENSIONES */}
              <section className={s.section}>
                <div className={s.sectionTitle}>PENSIONES</div>

                {showEmpty && (
                  <div className={s.empty}>
                    <p className={s.emptyText}>Sin pensiones registradas</p>
                  </div>
                )}

                {!showEmpty && (
                  <div className={s.pensionsScroll}>
                    <ul className={s.list}>
                      {items.map((it, idx) => (
                        <li key={`${it.negnom}-${it.cheque}-${idx}`} className={s.card}>
                          <div className={s.cardIcon}>
                            <Landmark size={16} />
                          </div>

                          <div className={s.cardBody}>
                            <p className={s.text}>{it.negnom}</p>

                            <div className={s.metaGrid}>
                              <div className={s.metaItem}>
                                <span className={s.metaLabel}>Cheque</span>
                                <span className={s.metaValue}>{pick(it.cheque)}</span>
                              </div>

                              <div className={s.metaItem}>
                                <span className={s.metaLabel}>Banco</span>
                                <span className={s.metaValue}>
                                  {it.bco && it.bco !== '0' ? it.bco : '—'}
                                </span>
                              </div>

                              <div className={s.metaItemWide}>
                                <span className={s.metaLabel}>Lugar de pago</span>
                                <span className={s.metaValue}>{pick(it.lugar_pago)}</span>
                              </div>

                              {it.n_cuenta && (
                                <div className={s.metaItemWide}>
                                  <span className={s.metaLabel}>Cuenta</span>
                                  <span className={s.metaValue}>{it.n_cuenta}</span>
                                </div>
                              )}
                            </div>
                          </div>

                          <div className={s.amountPill}>{money(it.cantidad)}</div>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
              </section>
            </>
          )}
        </div>
      </aside>
    </div>
  );
}