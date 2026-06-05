'use client';

import { CalendarDays, Clock3, Hash, Scroll, Banknote } from 'lucide-react';
import type { DavsEntregadoResponse } from '../../types/historicoEntregados.types';
import s from './DavsEntregadoBody.module.css';

type Props = {
  data: DavsEntregadoResponse;
};

function getValue(value?: string | number | null) {
  return value === null || value === undefined || String(value).trim() === ''
    ? '—'
    : String(value);
}

function getInitials(name?: string | null) {
  const safe = (name ?? '').trim();
  if (!safe) return 'SP';

  const parts = safe.split(/\s+/).filter(Boolean);
  return (
    parts
      .slice(0, 1)
      .map((p) => p[0]?.toUpperCase() ?? '')
      .join('') || 'SP'
  );
}

function formatMoney(value?: string | number | null) {
  const n =
    typeof value === 'number'
      ? value
      : Number(String(value ?? '').replace(/,/g, '').trim());

  if (!Number.isFinite(n)) return '—';

  return new Intl.NumberFormat('es-MX', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(n);
}
//fecha//
function formatDateCustom(value?: string | null) {
  if (!value) return '—';

  let date: Date;

  // 🔥 Caso 1: formato YYYY-MM-DD
  if (/^\d{4}-\d{2}-\d{2}/.test(value)) {
    const [year, month, day] = value.split('T')[0].split('-').map(Number);
    date = new Date(year, month - 1, day);
  }
  // 🔥 Caso 2: formato DD/MM/YYYY
  else if (/^\d{2}\/\d{2}\/\d{4}/.test(value)) {
    const [day, month, year] = value.split('/').map(Number);
    date = new Date(year, month - 1, day);
  }
  // 🔥 fallback
  else {
    const temp = new Date(value);
    if (isNaN(temp.getTime())) return value;
    date = temp;
  }

  const dayStr = String(date.getDate()).padStart(2, '0');

  const monthName = new Intl.DateTimeFormat('es-MX', {
    month: 'long',
  }).format(date);

  const yearStr = date.getFullYear();

  return `${dayStr}/${monthName.charAt(0).toUpperCase() + monthName.slice(1)}/${yearStr}`;
}
//hora//
function formatHour12(value?: string | null) {
  if (!value) return '—';

  const [hours, minutes] = String(value).split(':');

  if (!hours || !minutes) return value;

  const date = new Date();
  date.setHours(Number(hours), Number(minutes), 0, 0);

  return new Intl.DateTimeFormat('es-MX', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  }).format(date);
}

const toSentenceCase = (value?: string | null) => {
  if (!value) return '-'
  const normalized = value.toLocaleLowerCase();
  return normalized.charAt(0).toUpperCase() + normalized.slice(1);
}

export default function DavsEntregadoBody({ data }: Props) {
  return (
    <div className={s.wrapper}>
      <section className={s.card}>
        <div className={s.profile}>
          <div className="avatar">{getInitials(data.enFavorDe)}</div>

          <div className={s.profileContent}>
            <div className={s.profileMain}>
              <h3 className={s.name}>{getValue(data.enFavorDe)}</h3>

              <div className={s.metaChips}>
                <span className={s.metaChip}>C.S.P. {getValue(data.claveServidor)}</span>
                <span className={s.metaChip}>RFC: {getValue(data.rfc)}</span>
              </div>
            </div>

            <div className={s.metaBar}>
              <span className={s.metaBarItem}>
                <Hash size={14} />
                <span>Folio {getValue(data.folio)}</span>
              </span>

              <span className={s.metaBarItem}>
                <CalendarDays size={14} />
                <span>{formatDateCustom(data.fecha)}</span>
              </span>

              <span className={s.metaBarItem}>
                <Clock3 size={14} />
                <span>
                  {data.horaFinal
                    ? `${formatHour12(data.horaInicio)} • ${formatHour12(data.horaFinal)}`
                    : formatHour12(data.horaInicio)}
                </span>
              </span>
            </div>
          </div>
        </div>
      </section>


      <section className={s.card}>
        <div className={s.cardHeader}>
          <div className={s.cardHeaderLeft}>
            <div className={s.cardIcon}>
              <Scroll size={18} />
            </div>

            <div className={s.cardHeading}>
              <h4 className={s.cardTitle}>Recibo oficial</h4>
              <p className={s.cardSubtitle}>Documento de constancia de pago</p>
            </div>
          </div>
        </div>

        <div className={s.summaryGrid}>
          <div className={`${s.summaryItem} ${s.summaryItemHighlight}`}>
            <div className={s.summaryLabel}>Bueno por</div>
            <div className={s.summaryValue}>{formatMoney(data.importeII)}</div>
          </div>

          <div className={s.summaryItem}>
            <div className={s.summaryLabel}>Líquido</div>
            <div className={s.summaryValue}>{formatMoney(data.importeII)}</div>
          </div>
        </div>
      </section>

      <section className={s.card}>
        <div className={s.cardHeader}>
          <div className={s.cardHeaderLeft}>
            <div className={s.cardIcon}>
              <Banknote size={18} />
            </div>

            <div className={s.cardHeading}>
              <h3 className={s.cardTitle}>Concepto del pago</h3>
              <p className={s.cardSubtitle}>Detalle descriptivo del beneficio entregado</p>
            </div>
          </div>
        </div>

        <div className={s.messageBlock}>
          <div className={s.blockLabel}>Por concepto de</div>
          <div className={s.messageText}>{toSentenceCase(getValue(data.mensajeCompleto))}</div>
        </div>
      </section>
    </div>
  );
}