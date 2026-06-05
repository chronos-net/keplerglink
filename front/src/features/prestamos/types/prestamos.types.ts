// ===== RAW (tal cual viene de la API) =====
export type PrestamosRaw = {
  cabeseraPrestamos?: {
    neyemp?: string;
    negnom?: string;
    rfc?: string;
    fecha_in?: string;
  };
  descPrestamos?: Array<{
    fecha_in?: string;
    cve_ded?: string;
    puesto?: string;
    imp_total?: number;
    imp_renta?: number;
    saldo?: number;
    plazos?: number;
    qnas_x_pagar?: number;
    doc_ref?: string;
  }>;
};

// ===== NORMALIZADO (lo que usamos en la UI) =====
export type PrestamoHeader = {
  neyemp: string;
  nombre: string;
  rfc: string;
  fechaInicio: string;
};

export type PrestamoDetalle = {
  id: string;
  fechaInicio: string;
  claveDeduccion: string;
  puesto: string;
  importeTotal: number;
  rentaQuincenal: number;
  saldo: number;
  plazos: number;
  qnasPorPagar: number;
  docRef: string;
};

export type PrestamosResponse = {
  cabecera: PrestamoHeader;
  detalle: PrestamoDetalle[];
};

export type PrestamosRequest = {
  neyemp: string;
  periodo: string; // "13"
  anio: number;    // 2007
};

export function normalizePrestamos(raw: PrestamosRaw): PrestamosResponse {
  const cab = raw.cabeseraPrestamos ?? {};
  const detalle = raw.descPrestamos ?? [];

  return {
    cabecera: {
      neyemp: cab.neyemp ?? '',
      nombre: cab.negnom ?? '',
      rfc: cab.rfc ?? '',
      fechaInicio: cab.fecha_in ?? '',
    },
    detalle: detalle.map((d, index) => ({
      id: `${d.cve_ded ?? 'row'}-${index}`,
      fechaInicio: d.fecha_in ?? '',
      claveDeduccion: d.cve_ded ?? '',
      puesto: (d.puesto ?? '').trim(),
      importeTotal: d.imp_total ?? 0,
      rentaQuincenal: d.imp_renta ?? 0,
      saldo: d.saldo ?? 0,
      plazos: d.plazos ?? 0,
      qnasPorPagar: d.qnas_x_pagar ?? 0,
      docRef: d.doc_ref ?? '',
    })),
  };
}
