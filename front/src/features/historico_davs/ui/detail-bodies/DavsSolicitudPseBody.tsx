'use client';

import { DevsSolicitudResponse } from '../../types/historicoSolicitud.types';
import ui from './DavsSolicitudShared.module.css';

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
//mayus minus//
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

export default function DavsSolicitudPseBody({ data }: Props) {

  const {
    encabezado,
    estatus,
    resumenPago,
    mensaje,
    conceptos
  } = data;

  return (
    <div className={ui.wrapper}>
      <section className={ui.card}>
        <div className={ui.profile}>
          <div className="avatar">{getInitials(encabezado?.nombre)}</div>

          <div className={ui.profileMain}>
            <h4 className={ui.name}>{getValue(encabezado?.nombre)}</h4>

            <div className={ui.metaChips}>
              <span className={ui.metaChip}>RFC: {getValue(encabezado?.rfc)}</span>
              <span className={ui.metaChip}>
                Fecha: {formatDateCustom(encabezado?.fecha)}
              </span>
            </div>
          </div>
        </div>
      </section>

      <section className={ui.card}>
        <div className={ui.cardHeader}>
          <div className={ui.cardTitle}>Resumen del pago</div>

          {estatus ? (
            <div className={ui.statusWrap}>
              <span className={ui.statusLabel}>Estatus</span>
              <span className={ui.badgeSuccess}>
                {toSmartSentenceCase(estatus)}
              </span>
            </div>
          ) : null}
        </div>

        <div className={ui.totalWrap}>
          <div>
            <div className={ui.label}>
              {toSmartSentenceCase('MONTO TOTAL')}
            </div>
            <div className={ui.totalValue}>
              {formatMoney(resumenPago?.montoTotal)}
            </div>
          </div>
        </div>

        {resumenPago?.montoTotalTexto ? (
          <div className={ui.amountText}>
            {getValue(resumenPago.montoTotalTexto)}
          </div>
        ) : null}
      </section>

      <section className={ui.card}>
        <div className={ui.sectionTitle}>Mensaje</div>
        <div className={ui.messageBox}>
          {toSmartSentenceCase(getValue(mensaje?.textoCompleto))}
        </div>
      </section>

      <section className={ui.card}>
        <div className={ui.sectionTitle}>Conceptos</div>

        {conceptos?.length ? (
          <div className={ui.rowsWrap}>
            {conceptos.map((item, idx) => (
              <div key={`${item.descripcion}-${idx}`} className={ui.rowItem}>
                <div className={ui.rowTextBlock}>
                  <div className={ui.rowTitle}>{getValue(item.descripcion)}</div>
                </div>

                <div className={ui.rowAmounts}>
                  <span className={ui.amountValue}>{formatMoney(item.importe)}</span>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className={ui.emptyState}>No hay conceptos para mostrar.</div>
        )}
      </section>


    </div>
  );
}