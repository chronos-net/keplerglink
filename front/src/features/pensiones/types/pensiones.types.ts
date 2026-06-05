/* =========================================
   PENSIONES NOMINA (GET)
========================================= */
export type PensionesLite = {
    neyemp: string;
    negnom: string;
}

export type PensionesRequest = {
    neyemp: string;
    periodo: string;
    anio: number;
}

export type PensionItem = {
    negnom: string;
    bco: string;
    lugar_pago: string;
    cantidad: string;
    cheque: string;
    n_cuenta: string | null;
}

export type PensionCabeceraSimp = {
    periodo: string;
    anio: string;
    neyemp: string;
    adsc: string | null;
    leyenda_adscripcion: string | null;
    cheque: string | null;
    puesto: string | null;
    leyenda_puesto: string | null;
    percep: string | null;
    ded: string | null;
    neto: string | null;
    lug_pago: string | null;
    num_cuenta: string | null;
};

export type PensionesResponse = {
    pensionCabeceraSimp: PensionCabeceraSimp;
    pension: PensionItem[];
};


export type RawPensionResponse = | {
    pensionSimp?: {
        pensionCabeseraSimp?: Partial<PensionCabeceraSimp> | null; // 👈 SOLO ESTA
        pension?: PensionItem[] | null;
    } | null;
} | null | undefined;


const emptyCabecera = (params: PensionesRequest): PensionCabeceraSimp => ({
    periodo: String(params.periodo ?? ""),
    anio: String(params.anio ?? ""),
    neyemp: params.neyemp ?? "",
    adsc: null,
    leyenda_adscripcion: null,
    cheque: null,
    puesto: null,
    leyenda_puesto: null,
    percep: null,
    ded: null,
    neto: null,
    lug_pago: null,
    num_cuenta: null,
});

export const normalizePension = (
    raw: RawPensionResponse,
    params: PensionesRequest

): PensionesResponse => {

    const cab = raw?.pensionSimp?.pensionCabeseraSimp ?? null;
    const items = raw?.pensionSimp?.pension ?? null;

    const base = emptyCabecera(params);

    return {
        pensionCabeceraSimp: cab ? { ...base, ...cab } : base,
        pension: Array.isArray(items) ? items : [],
    };
};


/* =========================================
   PENSIONES GLINK (POST)
========================================= */

export type PensionesGlinkRequest = {
    neyemp : string;
}

export type RawPostPension = {
    clavesp: string
    nombresp: string
    rfc: string
    fechain: string
    nombrepension: string
    tipo_desc: string
    altaqna: number
    altano: number
    porcentaje: number
    importe: number
    referencia: string
};

export type ResponsePostPension = RawPostPension[];

export type ItemPostPension = {
    beneficiario: string
    tipo: string
    inicio: string
    porcentaje: number
    importe: number
    referencia: string
};

export type ServidorPublico = {
    clavesp: string
    nombre: string
    rfc: string
    fechaIngreso: string
};

export type DataPensiones = {
    servidor: ServidorPublico
    pensiones: ItemPostPension[]
};

export type StatsPensiones = {
  totalPensiones: number
  importeTotal: number
  porcentajePromedio: number
};

export const normalizePensionesGlink = (raw: unknown): DataPensiones => {
  const arr = Array.isArray(raw) ? (raw as RawPostPension[]) : [];

  if (!arr.length) {
    return {
      servidor: { clavesp: "", nombre: "", rfc: "", fechaIngreso: "" },
      pensiones: [],
    };
  }

  const first = arr[0];

  return {
    servidor: {
      clavesp: String(first.clavesp ?? ""),
      nombre: String(first.nombresp ?? "").trim(),
      rfc: String(first.rfc ?? ""),
      fechaIngreso: String(first.fechain ?? ""),
    },
    pensiones: arr.map((p) => ({
      beneficiario: String(p.nombrepension ?? "").trim(),
      tipo: String(p.tipo_desc ?? ""),
      inicio: `Q${Number(p.altaqna ?? 0)} / ${Number(p.altano ?? 0)}`,
      porcentaje: Number(p.porcentaje ?? 0),
      importe: Number(p.importe ?? 0),
      referencia: String(p.referencia ?? ""),
    })),
  };
};
