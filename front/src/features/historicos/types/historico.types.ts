// src/features/historicos/types/historico.types.ts
export type HistoricoRequest = {
  neyemp: string;
};

export type HistoricoLite = {
    neyemp: string;
    negnom: string;
}

//* ===== RAW (tal como viene del API) ===== */

type RawCabeceraHistorico = {
  neyemp?: unknown;
  negnom?: unknown;
  rfc?: unknown;

  // tu API trae sitsp, pero a veces puede venir con otro typo
  sitsp?: unknown;
  sitspp?: unknown;

  curp?: unknown;
  dep?: unknown;
  uresp?: unknown;
  npza?: unknown;
  cct?: unknown;
  nh?: unknown;

  // tu API trae catego (NO categ)
  catego?: unknown;
  categ?: unknown;

  perdeocupacion?: unknown;
  perdeocupacionFormato?: unknown;
  desc_puesto?: unknown;
};

type RawHistoricoDetalle = {
  dependencia?: unknown;
  uresponsable?: unknown;
  plaza?: unknown;
  cct?: unknown;
  nhoras?: unknown;
  thoras?: unknown;
  catego?: unknown;

  // tu API trae perocupacion (NO preocupacion)
  perocupacion?: unknown;
  preocupacion?: unknown;

  totalpercep?: unknown;
  motivobaja?: unknown;
  tipodeplaza?: unknown;
  idmb?: unknown;
  descripcion?: unknown;
};

export type RawHistoricoResponse = {
  cabeseraHistorico?: RawCabeceraHistorico;
  percepcionesHistorico?: { total?: unknown };
  descHistorico?: RawHistoricoDetalle[];
};

/* ===== Tipos normalizados para la app ===== */

export type HistoricoHeader = {
  neyemp: string;
  nombre: string;
  rfc: string;
  curp: string;
  situacion: string;
  dep?: string | null;
  uresp?: string | null;
  plaza?: string | null;
  cct?: string | null;
  horas?: string | null;
  categoria?: string | null;
  periodoOcupacion?: string | null;
  perdeocupacionFormato?: string | null;
  puesto?: string | null;
};

export type HistoricoDetalle = {
  dependencia: string;
  unidadResponsable: string;
  plaza: string;
  categoria: string;
  periodoOcupacion: string;
  totalPercepciones: number;
  horas: string;
  tipoPlaza: string;
  motivo: string;
};

export type HistoricoResponse = {
  header: HistoricoHeader;
  percepciones: { total: number };
  detalles: HistoricoDetalle[];
};

/* ===== Helpers internos ===== */

function toStringOr(value: unknown, fallback: string): string {
  if (value === null || value === undefined) return fallback;
  const s = String(value).trim();
  return s === '' ? fallback : s;
}

function toNullableString(value: unknown): string | null {
  if (value === null || value === undefined) return null;
  const s = String(value).trim();
  return s === '' ? null : s;
}

function toNumber(value: unknown): number {
  if (typeof value === 'number') return Number.isFinite(value) ? value : 0;
  if (value === null || value === undefined) return 0;
  const cleaned = String(value).replace(/[^\d.-]/g, '');
  const n = Number(cleaned);
  return Number.isFinite(n) ? n : 0;
}

/* ===== Normalizador ===== */

export function normalizeHistorico(raw: RawHistoricoResponse): HistoricoResponse {
  const cab = raw.cabeseraHistorico ?? {};

  const header: HistoricoHeader = {
    neyemp: toStringOr(cab.neyemp, ''),
    nombre: toStringOr(cab.negnom, ''),
    rfc: toStringOr(cab.rfc, ''),
    curp: toStringOr(cab.curp, ''),

    // ✅ FIX: sitsp vs sitspp
    situacion: toStringOr(cab.sitsp ?? cab.sitspp, ''),

    dep: toNullableString(cab.dep),
    uresp: toNullableString(cab.uresp),
    plaza: toNullableString(cab.npza),
    cct: toNullableString(cab.cct),
    horas: toNullableString(cab.nh),

    // ✅ FIX: catego vs categ
    categoria: toNullableString(cab.catego ?? cab.categ),
    periodoOcupacion: toNullableString(cab.perdeocupacion),
    perdeocupacionFormato: toNullableString(cab.perdeocupacionFormato),

    puesto: toNullableString(cab.desc_puesto),
  };

  const total = toNumber(raw.percepcionesHistorico?.total);

  const detalles: HistoricoDetalle[] = (raw.descHistorico ?? []).map(
    (row): HistoricoDetalle => ({
      dependencia: toStringOr(row.dependencia, '—'),
      unidadResponsable: toStringOr(row.uresponsable, '—'),
      plaza: toStringOr(row.plaza, '—'),
      categoria: toStringOr(row.catego, '—'),

      // ✅ FIX: perocupacion vs preocupacion
      periodoOcupacion: toStringOr(row.perocupacion ?? row.preocupacion, '—'),

      totalPercepciones: toNumber(row.totalpercep),
      horas: toStringOr(row.nhoras ?? row.thoras, '—'),
      tipoPlaza: toStringOr(row.tipodeplaza, '—'),
      motivo: toStringOr(row.descripcion ?? row.motivobaja, '—'),
    })
  );

  return {
    header,
    percepciones: { total },
    detalles,
  };
}
