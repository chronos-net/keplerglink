'use client';

import { CalendarDays, Clock3, Hash, ScrollText, Eye } from 'lucide-react';
import type { DavsAsesoriaResponse } from '../../types/historicoAsesoria.get.types';
import s from './DavsAsesoriaBody.module.css';


type Props = {
  data: DavsAsesoriaResponse;
};

type TramiteItem = {
  label: string;
  selected: boolean;
};

function getValue(value?: string | null) {
  return value && String(value).trim() ? value : '—';
}

function getInitials(name?: string | null) {
  const safe = (name ?? '').trim();
  if (!safe) return 'SP';

  const parts = safe.split(/\s+/).filter(Boolean);
  return parts
    .slice(0, 1)
    .map((p) => p[0]?.toUpperCase() ?? '')
    .join('') || 'SP';
}
// informacion de la tarjeta//
function InfoItem({ label, value }: { label: string; value?: string | null }) {
  return (
    <div className={s.infoItem}>
      <div className={s.infoLabel}>
        {toSentenceCase(label)}
      </div>

      <div className={s.infoValue}>
        {getValue(value) === '—' ? '—' : toSentenceCase(value)}
      </div>
    </div>
  );
}

function normalizeText(value?: string | null) {
  return String(value ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .trim()
    .toUpperCase();
}

function matchesTramite(value: string | null | undefined, expected: string) {
  return normalizeText(value) === normalizeText(expected);
}
//mayusculas minusculas//
function toSentenceCase(value?: string | null) {
  if (!value) return '—';
  // Si es puro número o código, no tocar
  if (/^[A-Z0-9.-]+$/.test(value)) return value;
  const text = value.toLowerCase();
  return text.charAt(0).toUpperCase() + text.slice(1);
}
//fecha//
function formatDateCustom(value?: string | null) {
  if (!value) return '—';

  const [year, month, day] = value.split('-').map(Number);

  // 👇 esto evita el problema de zona horaria
  const date = new Date(year, month - 1, day);

  const dayStr = String(day).padStart(2, '0');

  const monthName = new Intl.DateTimeFormat('es-MX', {
    month: 'long',
  }).format(date);

  const yearStr = year;

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

export default function DavsAsesoriaBody({ data }: Props) {
  const tramites: TramiteItem[] = [
    {
      label: 'PRIMA DE ANTIGÜEDAD',
      selected: matchesTramite(
        data.tramitesSolicitados.primaAntiguedad,
        'PRIMA DE ANTIGÜEDAD'
      ),
    },
    {
      label: 'PRIMA POR JUBILACIÓN',
      selected: matchesTramite(
        data.tramitesSolicitados.primaJubilacion,
        'PRIMA POR JUBILACIÓN'
      ),
    },
    {
      label: 'SEGURO DE VIDA',
      selected: matchesTramite(
        data.tramitesSolicitados.seguroVida,
        'SEGURO DE VIDA'
      ),
    },
    {
      label: 'PAGO DE BENEFICIOS INDIVIDUALES FOREMEX',
      selected: matchesTramite(
        data.tramitesSolicitados.pagosIndividualesForemex,
        'PAGO DE BENEFICIOS INDIVIDUALES FOREMEX'
      ),
    },
  ];

  return (
   <div className={s.wrapper}>
  <section className={s.card}>
    <div className={s.profile}>
      <div className="avatar">{getInitials(data.nombre)}</div>

      <div className={s.profileMain}>
        <h4 className={s.name}>{getValue(data.nombre)}</h4>

        <div className={s.metaChips}>
          <span className={s.metaChip}>
            Clave S.P. {getValue(data.neyemp)}
          </span>
          <span className={s.metaChip}>
            RFC: {getValue(data.rfc)}
          </span>
          <span className={s.metaChip}>
            ISSEMYM: {getValue(data.issemym)}
          </span>
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
              {formatHour12(data.horaInicial)} - {formatHour12(data.horaFinal)}
            </span>
          </span>
        </div>
      </div>
    </div>
        <div className={s.infoGrid}>
          <InfoItem label="Unidad administrativa" value={data.unidadAdministrativa} />
          <InfoItem label="Adscripción" value={data.direccionGeneral} />
          <InfoItem label="Dirección" value={data.direccion} />
          <InfoItem label="Tipo de sindicato" value={data.sindicato} />
          <InfoItem label="Departamento" value={data.departamento} />
          <InfoItem label="Puesto" value={data.puesto} />
          <InfoItem label="Lugar de pago" value={data.lugarPago} />
          <InfoItem label="Quincena" value={data.quincena} />
          <InfoItem label="Subsecretaría" value={data.subSecretaria} />
          <InfoItem label="Dirección de área" value={data.direccionArea} />
          <InfoItem label="Subdirección" value={data.subdireccion} />
          <InfoItem label="Nivel / rango" value={data.nivelRango} />
        </div>

        <div className={s.salaryBlock}>
          <div className={s.salaryLabel}>Sueldo base mensual</div>
          <div className={s.salaryValue}>{getValue(data.sueldoMensual)}</div>
        </div>
      </section>



      <section className={s.card}>
        <div className={s.cardHeader}>
          <div className={s.cardIcon}>
            <ScrollText size={20} />
          </div>
          <div>
            <h3 className={s.cardTitle}>Trámite solicitado</h3>
            <p className={s.cardSubtitle}>Conceptos incluidos en esta asesoría</p>
          </div>
        </div>

        <div className={s.tramitesList}>
          {tramites.map((item) => (
            <div
              key={item.label}
              className={`${s.tramiteRow} ${item.selected ? s.tramiteRowActive : ''}`}
            >
              <div className={s.tramiteLeft}>
                <span className={`${s.tramiteDot} ${item.selected ? s.tramiteDotActive : ''}`}>
                  {item.selected ? '✓' : ''}
                </span>

                <span className={s.tramiteLabel}>
                  {toSentenceCase(item.label)}
                </span>
              </div>

              {item.selected && <span className={s.tramiteBadge}>Seleccionado</span>}
            </div>
          ))}
        </div>
      </section>

      <section className={s.card}>
        <div className={s.cardHeader}>
          <div className={s.cardIcon}>
            <Eye size={20} />
          </div>
          <div>
            <h3 className={s.cardTitle}>Observaciones</h3>
            <p className={s.cardSubtitle}>Notas adicionales del registro</p>
          </div>
        </div>

        <div className={s.noteBox}>
          {getValue(data.comentarios) === '—'
            ? 'Sin observaciones registradas.'
            : toSentenceCase(data.comentarios)}
        </div>
      </section>
    </div>
  );
}