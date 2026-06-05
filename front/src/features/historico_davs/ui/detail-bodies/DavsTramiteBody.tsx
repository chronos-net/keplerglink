'use client';

import { CalendarDays, Clock3, Hash, Hourglass } from 'lucide-react';
import { DavsTramiteResponse } from '../../types/historicoTramite.get.types';
import { formatDateISO } from '../../utils/formateDateIso';
import s from './DavsTramiteBody.module.css';

type Props = {
  data: DavsTramiteResponse;
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

function toSmartSentenceCase(value?: string | null) {
  if (!value) return '—';

  const text = value.trim();
  if (!text) return '—';

  return text
    .toLowerCase()
    .replace(/^./, (c) => c.toUpperCase());
}

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

  if (isNaN(date.getTime())) return value;

  const day = String(date.getDate()).padStart(2, '0');
  const month = new Intl.DateTimeFormat('es-MX', { month: 'long' }).format(date);
  const year = date.getFullYear();

  return `${day}/${month.charAt(0).toUpperCase() + month.slice(1)}/${year}`;
}

function formatHour12(value?: string | number | null) {
  if (value === null || value === undefined || String(value).trim() === '') return '—';

  const raw = String(value).trim();
  const match = raw.match(/^(\d{1,2}):(\d{2})/);

  if (!match) return raw;

  const hours = Number(match[1]);
  const minutes = Number(match[2]);

  if (Number.isNaN(hours) || Number.isNaN(minutes)) return raw;

  const date = new Date();
  date.setHours(hours, minutes, 0, 0);

  return new Intl.DateTimeFormat('es-MX', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  }).format(date);
}

// 🔥 FIX QUINCENA
function formatQuincena(value?: string | null) {
  if (!value) return '—';

  return value
    .replace(/([a-zA-Z])(\d)/g, '$1 $2')
    .replace(/del(\d)/gi, 'del $1')
    .replace(/(\d)del/gi, '$1 del')
    .replace(/\s+/g, ' ')
    .trim();
}

export default function DavsTramiteBody({ data }: Props) {
  return (
    <div className={s.wrapper}>

      {/* 🔹 CARD 1: PERFIL */}
      <section className={s.card}>
        <div className={s.profile}>
          <div className="avatar">{getInitials(data.nombre)}</div>

          <div className={s.profileMain}>
            <h4 className={s.name}>{getValue(data.nombre)}</h4>

            <div className={s.metaChips}>
              <span className={s.metaChip}>C.S.P. {getValue(data.claveServidor)}</span>
              <span className={s.metaChip}>RFC: {getValue(data.rfc)}</span>
              <span className={s.metaChip}>ISSEMYM: {getValue(data.issemym)}</span>
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
      </section>

      {/* 🔹 CARD 2: RESUMEN */}
      <section className={s.card}>
        <section className={s.resumeStrip}>
          <div className={s.resumeItem}>
            <div className={s.resumeLabel}>Unidad adm.</div>
            <div className={s.resumeValue}>{getValue(data.unidadAdministrativa)}</div>
          </div>

          <div className={s.resumeItem}>
            <div className={s.resumeLabel}>Folio asesoría</div>
            <div className={s.resumeValue}>{getValue(data.folioAsesoria)}</div>
          </div>

          <div className={s.resumeItem}>
            <div className={s.resumeLabel}>Lugar de pago</div>
            <div className={s.resumeValue}>{getValue(data.lugarDePago)}</div>
          </div>

          <div className={s.resumeItem}>
            <div className={s.resumeLabel}>Quincena</div>
            <div className={`${s.resumeValue} ${s.resumeValueWrap}`}>
              {formatQuincena(toSmartSentenceCase(getValue(data.quincena)))}
            </div>
          </div>
        </section>
      </section>

      {/* 🔹 CARD 3: INFORMACIÓN */}
      <section className={s.card}>
        <div className={s.infoGrid}>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Dirección</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.direccion))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Tel. oficina</div>
            <div className={s.infoValue}>{getValue(data.telefonoOficina)}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Tel. particular</div>
            <div className={s.infoValue}>{getValue(data.telefonoParticular)}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Tipo de sindicato</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.tipoDeSindicato))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Fecha de baja</div>
            <div className={s.infoValue}>{formatDateISO(data.fechaBaja)}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Motivo de separación</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.motivoSeparacion))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Dependencia</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.direccionGeneral))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Subsecretaría</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.subsecretaria))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Departamento</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.departamento))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Puesto</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.puesto))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Nivel / rango</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.nivelRango))}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Sueldo base mensual</div>
            <div className={s.infoValue}>{getValue(data.sueldoBaseMensual)}</div>
          </div>

          <div className={s.infoItem}>
            <div className={s.infoLabel}>Tipo de seguro</div>
            <div className={s.infoValue}>{toSmartSentenceCase(getValue(data.tipoSeguro))}</div>
          </div>

        </div>
      </section>

      {/* 🔹 CARD 4: ANTIGÜEDAD */}
      <section className={s.card}>
        <div className={s.cardHeader}>
          <div className={s.cardIcon}>
            <Hourglass size={18} />
          </div>

          <div>
            <h3 className={s.cardTitle}>Antigüedad</h3>
            <p className={s.cardSubtitle}>Resumen de cálculo e importes</p>
          </div>
        </div>

        <div className={s.antiguedadTable}>
          <div className={s.antiguedadHead}>Concepto</div>
          <div className={s.antiguedadHead}>Inicio</div>
          <div className={s.antiguedadHead}>Fin</div>
          <div className={s.antiguedadHead}>Años</div>
          <div className={s.antiguedadHead}>Meses</div>

          <div className={s.antiguedadCell}>{getValue(data.antiguedadConcepto)}</div>
          <div className={s.antiguedadCell}>{getValue(data.antiguedadFechaInicio)}</div>
          <div className={s.antiguedadCell}>{getValue(data.antiguedadFechaFin)}</div>
          <div className={s.antiguedadCell}>{getValue(data.antiguedadAnios)}</div>
          <div className={s.antiguedadCell}>{getValue(data.antiguedadMeses)}</div>
        </div>

        <div className={s.antiguedadResume}>
          <div className={s.resumeRow}>
            <span>Antigüedad efectiva:</span>
            <strong>{getValue(data.antiguedadEfectiva)}</strong>
          </div>

          <div className={s.resumeRow}>
            <span>Total años cumplidos efectivos:</span>
            <strong>{getValue(data.totalAniosCumplidos)}</strong>
          </div>
        </div>

        <div className={s.amountGrid}>
          <div className={s.amountItem}>
            <div className={s.amountLabel}>Importe con sueldo base</div>
            <div className={s.amountValue}>{getValue(data.importeConSueldoBase)}</div>
          </div>

          <div className={s.amountItem}>
            <div className={s.amountLabel}>Importe con salario mínimo</div>
            <div className={s.amountValue}>{getValue(data.importeConSalarioMinimo)}</div>
          </div>

          <div className={s.amountItem}>
            <div className={s.amountLabel}>Importe a pagar</div>
            <div className={s.amountValue}>{getValue(data.importePagar)}</div>
          </div>
        </div>
      </section>

    </div>
  );
}