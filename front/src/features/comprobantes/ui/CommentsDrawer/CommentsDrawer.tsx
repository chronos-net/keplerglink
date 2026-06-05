import { X, CalendarDays, MessageSquareText } from 'lucide-react';
import s from './comments-drawer.module.css';

import type {
  ComentariosCatalogo,
  GetComentariosCabecera,
  GetComentariosValor,
} from '@/features/comentarios/types/comentarios.get.types';

type Props = {
  open: boolean;
  onClose: () => void;
  loading: boolean;
  error: string | null;
  items: GetComentariosValor[];
  header?: GetComentariosCabecera | null;
  periodo?: string;
  anio?: string;
};

const pick = (v?: string | null) => (v && String(v).trim().length ? String(v) : '—');

const pickCatalog = (v?: ComentariosCatalogo | null) => {
  const clave = String(v?.clave ?? '').trim();
  const descripcion = String(v?.descripcion ?? '').trim();

  if (clave && descripcion) return `${clave} - ${descripcion}`;
  if (clave) return clave;
  if (descripcion) return descripcion;

  return '-';
};

const pickNumber = (v?: number | null) => {
  return typeof v === 'number' && !Number.isNaN(v)
    ? v.toLocaleString('es-MX', { style: 'currency', currency: 'MXN' })
    : null;
};

const sumMoney = (
  items: GetComentariosValor[],
  key: 'importeInicial' | 'importeFinal' | 'diferencia'
) => {
  return items.reduce((total, item) => {
    const value = item[key];
    return total + (typeof value === 'number' && !Number.isNaN(value) ? value : 0);
  }, 0);
};

const findDate = (text: string): string | null => {
  const m = text.match(/\b(\d{2}\/\d{2}\/\d{4})\b/);
  return m ? m[1] : null;
};

const formatDate = (value?: string | null) => {
  const raw = value?.trim();
  if (!raw) return null;

  const [datePart] = raw.split(' ');
  const [year, month, day] = datePart.split('-');
  if (!year || !month || !day) return raw;

  return `${day}/${month}/${year}`;
};

export function LegacyCommentsDrawer({
  open,
  onClose,
  loading,
  error,
  items,
  header,
  periodo,
  anio,
}: Props) {
  if (!open) return null;

  const showEmpty = !loading && !error && items.length === 0;
  const firstItem = items[0] ?? null;

  const periodoValue = periodo ?? firstItem?.qna;
  const anioValue = anio ?? firstItem?.anio;

  const totalInicial = sumMoney(items, 'importeInicial');
  const totalFinal = sumMoney(items, 'importeFinal');
  const totalDiferencia = sumMoney(items, 'diferencia');

  return (
    <div className={s.backdrop} role="dialog" aria-modal="true">
      <aside className={s.sheet}>
        <header className={s.header}>
          <div className={s.headerLeft}>
            <div className={s.logo}>PAYROLL<br />HR</div>
            <div>
              <h3 className={s.title}>Detalles del Movimiento</h3>
              <div className={s.subtitle}>Comentarios del recibo seleccionado</div>
            </div>
          </div>

          <button className={s.closeBtn} onClick={onClose} aria-label="Cerrar">
            <X size={18} />
          </button>
        </header>

        <div className={s.body}>
          {loading && (
            <div className={s.state}>
              <div className={s.skelTitle} />
              <div className={s.skelRow} />
              <div className={s.skelRow} />
              <div className={s.skelCard} />
              <div className={s.skelCard} />
            </div>
          )}

          {!loading && error && (
            <div className={s.error}>
              <b>No se pudieron cargar comentarios.</b>
              <div className={s.errorMsg}>{String(error)}</div>
            </div>
          )}

          {!loading && !error && (
            <>
              <section className={s.section}>
                <div className={s.sectionTitle}>RESUMEN</div>

                <div className={s.summaryCard}>
                  <div className={s.qnaBar}>
                    <CalendarDays size={18} />
                    <div className={s.qnaBarText}>
                      <div className={s.qnaMain}>
                        Periodo {pick(periodoValue)} / Año {pick(anioValue)}
                      </div>
                      <div className={s.qnaSub}>
                        Clave de Servidor Público: {pick(header?.neyemp)}
                      </div>
                    </div>
                  </div>

                  <div className={s.summaryGrid}>
                    <div>
                      <div className={s.label}>Nombre</div>
                      <div className={s.value}>{pick(header?.nombreCompleto)}</div>
                    </div>

                    <div>
                      <div className={s.label}>Adscripción</div>
                      <div className={s.value}>{pickCatalog(firstItem?.adscripcion)}</div>
                    </div>

                    <div>
                      <div className={s.label}>Puesto</div>
                      <div className={s.value}>{pickCatalog(firstItem?.puesto)}</div>
                    </div>

                    <div>
                      <div className={s.label}>Cuenta</div>
                      <div className={s.valueMono}>{pick(firstItem?.numCuenta)}</div>
                    </div>
                  </div>
                </div>
              </section>

              <section className={s.section}>
                <div className={s.sectionTitle}>MONTOS</div>

                <div className={s.kpiRow}>
                  <div className={s.kpi}>
                    <div className={s.kpiLabel}>Importe inicial</div>
                    <div className={s.kpiValue}>{pickNumber(totalInicial)}</div>
                  </div>

                  <div className={s.kpi}>
                    <div className={s.kpiLabel}>Importe final</div>
                    <div className={s.kpiValueDim}>{pickNumber(totalFinal)}</div>
                  </div>

                  <div className={s.kpi}>
                    <div className={s.kpiLabel}>Diferencia</div>
                    <div className={s.kpiValue}>{pickNumber(totalDiferencia)}</div>
                  </div>
                </div>
              </section>

              <section className={s.section}>
                <div className={s.sectionTitle}>COMENTARIOS</div>

                {showEmpty && (
                  <div className={s.empty}>
                    <div className={s.emptyIcon}>!</div>
                    <p className={s.emptyText}>Sin comentarios</p>
                  </div>
                )}

                {!showEmpty && (
                  <ul className={s.list}>
                    {items.map((c, i) => {
                      const date = findDate(c.comentario ?? '') ?? formatDate(c.fechaCaptura);
                      const amount = pickNumber(c.importeInicial);

                      return (
                        <li key={c.id ?? i} className={s.card}>
                          <div className={s.cardIcon}>
                            <MessageSquareText size={16} />
                          </div>

                          <div className={s.cardBody}>
                            <p className={s.text}>{pick(c.comentario)}</p>
                            <div className={s.metaRow}>
                              {date && <span className={s.dateBadge}>{date}</span>}
                              {amount && <span className={s.dateBadge}>{amount}</span>}
                              {c.capturado && <span className={s.dateBadge}>Capturó: {c.capturado}</span>}
                              {c.lugarPago && <span className={s.dateBadge}>{pickCatalog(c.lugarPago)}</span>}
                            </div>
                          </div>
                        </li>
                      );
                    })}
                  </ul>
                )}
              </section>
            </>
          )}
        </div>
      </aside>
    </div>
  );
}

export { default } from './CommentsDrawerView';
