// src/features/comprobantes/types/payroll.types.ts
export type AnualizadoRequest = {
  anio: number;
  quincena?: string;
  neyemp?: string;
  nombreSp?: string;
};


export type AnualizadoEstatus = "OK" | "SIN_PLAZA" | "SIN_PAGO" | (string & {});

export type ApiEmpleado = {
  id: string;
  nombre: string;
  curp: string;
  rfc: string;
  issemym: string;
};

export type ApiResumenGlobal = {
  periodosConPago: number;
  periodosSinPlaza: number;
  primerPeriodo: string; // "01"
  ultimoPeriodo: string; // "A2"
  totalPercepciones: number;
  totalDeducciones: number;
  granTotalNeto: number;
};

export type ApiCatalogos = {
  percepciones?: { percepciones?: Record<string, string> };
  deducciones?: { deducciones?: Record<string, string> };
};

export type ApiPlazaItem = {
  periodo: string;
  plaza: string | null;
  ads: string | null;
  cheque: string | null;
  categoria: string | null;
  lugarPago: string | null;
  centroTrabajo: string | null;
  leyendaPuesto: string | null;
};

export type ApiConceptoImporte = {
  codigo: string;   // "1322" o "0402" etc
  importe: number;
};

export type ApiPeriodoItem = {
  periodo: string;  // "01"..."24" | "A2"
  plazaRef: string;
  estatus: AnualizadoEstatus;
  resumen: {
    percepciones: number;
    deducciones: number;
    neto: number;
  };
  percepciones: ApiConceptoImporte[];
  deducciones: ApiConceptoImporte[];
};

/** API RAW (tal cual backend) */
export type RawAnualizadoResponse = {
  empleado: ApiEmpleado;
  resumenGlobal: ApiResumenGlobal;
  catalogos?: ApiCatalogos;
  plazas?: ApiPlazaItem[];
  periodos?: ApiPeriodoItem[];
};

/** UI Model (normalizado, seguro para UI) */
export type CatalogosUI = {
  percepcionesByCodigo: Record<string, string>;
  deduccionesByCodigo: Record<string, string>;
};

export type AnualizadoResponse = {
  empleado: ApiEmpleado;
  resumenGlobal: ApiResumenGlobal;
  catalogos: CatalogosUI;
  plazas: ApiPlazaItem[];
  periodos: ApiPeriodoItem[];
};

function asRecordString(v: unknown): Record<string, string> {
  if (!v || typeof v !== "object") return {};
  const o = v as Record<string, unknown>;
  const out: Record<string, string> = {};
  for (const k of Object.keys(o)) {
    const val = o[k];
    if (typeof val === "string") out[k] = val;
  }
  return out;
}

export function normalizeAnualizado(raw: RawAnualizadoResponse): AnualizadoResponse {
  const perceps = asRecordString(raw.catalogos?.percepciones?.percepciones);
  const deducs = asRecordString(raw.catalogos?.deducciones?.deducciones);

  return {
    empleado: raw.empleado,
    resumenGlobal: raw.resumenGlobal,
    catalogos: {
      percepcionesByCodigo: perceps,
      deduccionesByCodigo: deducs,
    },
    plazas: Array.isArray(raw.plazas) ? raw.plazas : [],
    periodos: Array.isArray(raw.periodos) ? raw.periodos : [],
  };
}