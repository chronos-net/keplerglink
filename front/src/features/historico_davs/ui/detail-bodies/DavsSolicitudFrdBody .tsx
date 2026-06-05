'use client';

import { DevsSolicitudResponse } from '../../types/historicoSolicitud.types';
import shared from './DavsSolicitudShared.module.css';
import s from './DavsSolicitudFrdBody.module.css';
import { HandCoins, MessageSquareMore, MessageSquareDashed } from 'lucide-react';

type Props = {
  data: DevsSolicitudResponse;
};

function getValue(value?: string | null) {
  const v = (value ?? '').trim();
  return v || '—';
}

function getInitials(value?: string | null) {
  const text = (value ?? '').trim();
  if (!text) return '—';

  return text[0]?.toUpperCase() ?? '—';
}

function formatMoney(value?: number | null) {
  if (value == null || Number.isNaN(value)) return '—';

  return new Intl.NumberFormat('es-MX', {
    style: 'currency',
    currency: 'MXN',
    minimumFractionDigits: 2,
  }).format(value);
}
//mayus-minus//
function toSmartSentenceCase(value?: string | null) {
  if (!value) return '—';

  const text = value.trim();
  if (!text) return '—';

  return text
    .toLowerCase()
    .replace(/^./, (c) => c.toUpperCase());
}
//fecha//
function formatDateCustom(value?: string | null) {
  if (!value) return '—';

  let date: Date;

  if (/^\d{4}-\d{2}-\d{2}/.test(value)) {
    const [year, month, day] = value.split('T')[0].split('-').map(Number);
    date = new Date(year, month - 1, day);
  } else if (/^\d{2}\/\d{2}\/\d{4}/.test(value)) {
    const [day, month, year] = value.split('/').map(Number);
    date = new Date(year, month - 1, day);
  } else {
    const temp = new Date(value);
    if (isNaN(temp.getTime())) return value;
    date = temp;
  }

  const day = String(date.getDate()).padStart(2, '0');
  const month = new Intl.DateTimeFormat('es-MX', { month: 'long' }).format(date);
  const year = date.getFullYear();

  return `${day}/${month.charAt(0).toUpperCase() + month.slice(1)}/${year}`;
}

export default function DavsSolicitudFrdBody({ data }: Props) {

  console.log(data)

  const {
    encabezado,
    estatus,
    resumenPago,
    mensaje,
    conceptos,
    movimientosComplementarios,
  } = data;

  return (
    <div className={shared.wrapper}>
      <section className={shared.card}>
        <div className={shared.profile}>
          <div className="avatar">{getInitials(encabezado?.nombre)}</div>

          <div className={shared.profileMain}>
            <h4 className={shared.name}>{getValue(encabezado?.nombre)}</h4>

            <div className={shared.metaChips}>
              <span className={shared.metaChip}>RFC: {getValue(encabezado?.rfc)}</span>
              <span className={shared.metaChip}>
                Fecha: {formatDateCustom(encabezado?.fecha)}
              </span>
            </div>
          </div>
        </div>
      </section>

      <section className={shared.card}>
        <div className={shared.cardHeader}>
          <div className={shared.cardHeaderLeft}>
            <div className={shared.cardIcon}>
              <HandCoins size={14} />
            </div>

            <div className={shared.cardHeading}>
              <div className={shared.cardTitle}>Resumen del pago</div>
            </div>
          </div>

          {estatus ? (
            <div className={shared.statusWrap}>
              <span className={shared.statusLabel}>Estatus</span>
              <span className={shared.badgeSuccess}>
                {toSmartSentenceCase(estatus)}
              </span>
            </div>
          ) : null}
        </div>

        <div className={shared.summaryGrid}>
          <div className={shared.summaryItem}>
            <div className={shared.summaryLabel}>Monto total</div>
            <div className={shared.summaryValue}>
              {formatMoney(resumenPago?.montoTotal)}
            </div>
          </div>
        </div>

        {resumenPago?.montoTotalTexto ? (
          <div className={shared.amountText}>
            {getValue(resumenPago.montoTotalTexto)}
          </div>
        ) : null}
      </section>

      <section className={shared.card}>
        <div className={shared.cardHeader}>
          <div className={shared.cardHeaderLeft}>
            <div className={shared.cardIcon}>
              <MessageSquareMore size={14} />
            </div>

            <div className={shared.cardHeading}>
              <div className={shared.cardTitle}>Mensaje</div>
            </div>
          </div>
        </div>

        <div className={shared.messageBox}>
          {toSmartSentenceCase(getValue(mensaje?.textoCompleto))}
        </div>
      </section>

      <section className={shared.card}>
        <div className={shared.cardHeader}>
          <div className={shared.cardHeaderLeft}>
            <div className={shared.cardIcon}>
              <MessageSquareDashed size={14} />
            </div>

            <div className={shared.cardHeading}>
              <div className={shared.cardTitle}>Conceptos</div>
            </div>
          </div>
        </div>

        {conceptos?.length ? (
          <div className={s.rowsWrap}>
            {conceptos.map((item, idx) => (
              <div key={`${item.descripcion}-${idx}`} className={s.rowItem}>
                <div className={s.rowTextBlock}>
                  <div className={s.rowTitle}>{getValue(item.descripcion)}</div>
                </div>

                <div className={s.rowAmounts}>
                  <span className={s.amountValue}>{formatMoney(item.importe)}</span>
                  <span className={s.amountValue}>{formatMoney(item.importe2)}</span>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className={s.emptyState}>No hay conceptos para mostrar.</div>
        )}
      </section>

      {movimientosComplementarios?.length ? (
        <section className={shared.card}>
          <div className={shared.sectionTitle}>Movimientos complementarios</div>

          <div className={s.rowsWrap}>
            {movimientosComplementarios.map((item, idx) => (
              <div key={`${item.descripcion}-${idx}`} className={s.rowItem}>
                <div className={s.rowTextBlock}>
                  <div className={s.rowTitle}>{getValue(item.descripcion)}</div>
                </div>

                <div className={s.rowAmounts}>
                  <span className={s.amountValue}>{formatMoney(item.importe)}</span>
                  <span className={s.amountValue}>{formatMoney(item.importe2)}</span>
                </div>
              </div>
            ))}
          </div>
        </section>
      ) : null}
    </div>
  );
}
