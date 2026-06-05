export type PrestamosGlinkRequest = {
  neyemp: string;
  periodo: string; // "01".."24"
  anio: number;
};

export type PrestamosCabecera = {
  neyemp: string;
  negnom: string;
  rfc: string;
  fecha_in?: string; // "01-02-1977"
};

export type PrestamoItem = {
  fecha_in?: string;
  cve_ded?: string;
  puesto?: string;

  imp_total?: number;
  imp_renta?: number;
  saldo?: number;

  plazos?: number;
  qnas_x_pagar?: number;
  doc_ref?: string;
} & Record<string, unknown>; // ✅ extra fields sin any

export type PrestamosGlinkResponse = {
  cabeceraPrestamos?: PrestamosCabecera; // ✅ opcional por seguridad
  descPrestamos?: PrestamoItem[];        // ✅ opcional por seguridad

  // ✅ typo del backend (para no romper)
  cabeseraPrestamos?: PrestamosCabecera;
};

export function splitNombreOClave(
  s?: string
): { neyemp?: string; nombreSp?: string } {
  const t = (s ?? '').trim();
  if (!t) return {};
  return /^\d{6,}$/.test(t) ? { neyemp: t } : { nombreSp: t };
}

export function clampPeriodo(v?: string) {
  const n = Math.max(
    1,
    Math.min(24, Number((v ?? '').replace(/\D/g, '') || '0'))
  );
  return String(n).padStart(2, '0');
}
export type CabeceraPrestamos = PrestamosCabecera;
