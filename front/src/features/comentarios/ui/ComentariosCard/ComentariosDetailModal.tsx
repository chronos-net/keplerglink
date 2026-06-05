import type { ReactNode } from "react";
import {
  Building2,
  CalendarDays,
  CheckCircle2,
  ChevronDown,
  FileText,
  IdCard,
  MapPin,
  MessageSquareText,
  X,
} from "lucide-react";

import s from "./ComentariosDetailModal.module.css";

import type {
  PostCommentItem,
  PostCommentsCatalogo,
  PostCommentsHeader,
} from "../../types/comentarios.post.types";

import {
  formatMoney,
  formatPeriodo,
  formatShortDate,
  textOrDash,
} from "../../utils/comentariosCard.utils";

type Props = {
  item: PostCommentItem | null;
  cabecera: PostCommentsHeader | null;
  onClose: () => void;
};

type InfoItemProps = {
  label: string;
  value: string;
};

type CatalogRowProps = {
  icon: ReactNode;
  label: string;
  value?: PostCommentsCatalogo | null;
};

const formatCatalog = (value?: PostCommentsCatalogo | null) => {
  const clave = textOrDash(value?.clave);
  const descripcion = textOrDash(value?.descripcion);

  if (clave === "-" && descripcion === "-") return { clave: "-", descripcion: "-" };

  return { clave, descripcion };
};

function AmountBox({
  label,
  value,
  tone = "default",
}: {
  label: string;
  value: number | null | undefined;
  tone?: "default" | "danger" | "success";
}) {
  return (
    <div className={`${s.amountBox} ${s[`amountBox_${tone}`]}`}>
      <span>{label}</span>
      <strong>{formatMoney(value)}</strong>
    </div>
  );
}

function InfoItem({ label, value }: InfoItemProps) {
  return (
    <div className={s.infoItem}>
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}

function CatalogRow({ icon, label, value }: CatalogRowProps) {
  const catalog = formatCatalog(value);

  return (
    <div className={s.catalogRow}>
      <div className={s.catalogIcon}>{icon}</div>
      <div className={s.catalogContent}>
        <span>{label}</span>
        <div className={s.catalogValue}>
          <strong>{catalog.clave}</strong>
          <p>{catalog.descripcion}</p>
        </div>
      </div>
    </div>
  );
}

export default function ComentariosDetailModal({
  item,
  cabecera,
  onClose,
}: Props) {
  if (!item) return null;

  const capturado = textOrDash(item.capturado);
  const hasCapturado = capturado !== "-";
  const fechaCaptura = formatShortDate(item.fechaCaptura);
  const hasComment = textOrDash(item.comentario) !== "-";
  const differenceTone = Number(item.diferencia) < 0 ? "danger" : "success";
  const initial = textOrDash(cabecera?.nombreCompleto).charAt(0).toUpperCase();

  return (
    <div className={s.modalBackdrop} role="dialog" aria-modal="true">
      <section className={s.modal} onClick={(event) => event.stopPropagation()}>
        <header className={s.modalHeader}>
          <div>
            <h3 className={s.modalTitle}>Detalles del Movimiento</h3>
            <p className={s.modalSubtitle}>Comentarios del recibo seleccionado</p>
          </div>

          <button
            className={s.closeButton}
            type="button"
            aria-label="Cerrar detalle"
            onClick={onClose}
          >
            <X size={18} />
          </button>
        </header>

        <div className={s.modalBody}>
          <section className={s.heroCard}>
            <div className={s.avatar}>{initial}</div>

            <div className={s.heroContent}>
              <h4 className={s.heroName}>{textOrDash(cabecera?.nombreCompleto)}</h4>

              <div className={s.heroMeta}>
                <div className={s.heroMetaItem}>
                  <IdCard size={14} />
                  <span>Clave:</span>
                  <strong>{textOrDash(cabecera?.neyemp)}</strong>
                </div>

                <div className={s.heroMetaItem}>
                  <IdCard size={14} />
                  <span>Curp:</span>
                  <strong>{textOrDash(cabecera?.curp)}</strong>
                </div>

                <div className={s.heroMetaItem}>
                  <CalendarDays size={14} />
                  <span>Quincena:</span>
                  <strong>{formatPeriodo(item).replace("QNA ", "")}</strong>
                </div>
              </div>
            </div>
          </section>

          <section className={s.section}>
            <div className={s.sectionHeader}>
              <h5>Importes del periodo</h5>
            </div>

            <div className={s.amountGrid}>
              <AmountBox label="Inicial" value={item.importeInicial} />
              <AmountBox label="Final" value={item.importeFinal} />
              <AmountBox
                label="Diferencia"
                value={item.diferencia}
                tone={differenceTone}
              />
            </div>
          </section>

          <section className={s.section}>
            <div className={s.sectionHeader}>
              <h5>Comentarios</h5>
            </div>

            <article className={s.recordCard}>
              <div className={s.recordTop}>
                <div className={s.movementPill}>
                  <span>Movimiento:</span>
                  <strong>{textOrDash(item.movimiento)}</strong>
                </div>

                <div
                  className={`${s.statusPill} ${hasComment ? s.statusPill_ok : s.statusPill_empty
                    }`}
                >
                  {hasComment ? (
                    <>
                      <span>Con comentario</span>
                      <CheckCircle2 size={18} />
                    </>
                  ) : (
                    <>
                      <span>Sin comentario</span>
                      <MessageSquareText size={17} />
                    </>
                  )}
                </div>
              </div>

              <div className={s.infoGrid}>
                <InfoItem
                  label="Forma de pago"
                  value={textOrDash(item.formaPago)}
                />
                <InfoItem
                  label="Cuenta / cheque"
                  value={textOrDash(item.numCuenta)}
                />
                <InfoItem
                  label="Fecha captura"
                  value={fechaCaptura}
                />
              </div>

            

              <div className={s.commentArea}>
                {hasComment ? (
                  <div className={s.commentCard}>
                    <div className={s.commentFooter}>
                      {hasCapturado ? (
                        <span>
                          Capturo: <strong>{capturado}</strong>
                        </span>
                      ) : (
                        <span>Comentario registrado</span>
                      )}
                    </div>

                    <p className={s.commentText}>{textOrDash(item.comentario)}</p>
                  </div>
                ) : (
                  <div className={s.emptyComment}>
                    <MessageSquareText size={15} />
                    Sin comentario registrado
                  </div>
                )}
              </div>

              <details className={s.adminDetails}>
                <summary className={s.adminSummary}>
                  <div className={s.adminSummaryTitle}>
                    <FileText size={19} />
                    <span>Datos administrativos</span>
                  </div>

                  <span className={s.adminChevron}>
                    <ChevronDown size={18} />
                  </span>
                </summary>

                <div className={s.adminBody}>
                  <CatalogRow
                    icon={<Building2 size={16} />}
                    label="Adscripcion"
                    value={item.adscripcion}
                  />
                  <CatalogRow
                    icon={<FileText size={16} />}
                    label="Puesto"
                    value={item.puesto}
                  />
                  <CatalogRow
                    icon={<MapPin size={16} />}
                    label="Lugar de pago"
                    value={item.lugarPago}
                  />

                </div>
              </details>
            </article>
          </section>
        </div>
      </section>
    </div>
  );
}
