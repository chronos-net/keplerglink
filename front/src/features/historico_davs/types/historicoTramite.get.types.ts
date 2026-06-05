export type RequestDavsTramite = {
  cveKdm1: number;
};

export type DavsTramiteResponse = {
  unidadAdministrativa: string;
  folio: string;
  folioAsesoria: string;
  fecha: string;
  fechaBaja: string;
  horaInicial: string;
  horaFinal: string;
  claveServidor: string;
  nombre: string;
  rfc: string;
  issemym: string;
  direccion: string;
  telefonoOficina: string;
  telefonoParticular: string;
  lugarDePago: string;
  tipoDeSindicato: string;
  motivoSeparacion: string;
  tipoSeguro: string;
  sueldoBase: string;
  quincena: string;
  subsecretaria: string;
  direccionGeneral: string;
  direccionDeArea: string;
  subdireccion: string;
  departamento: string;
  puesto: string;
  nivelRango: string;
  sueldoBaseMensual: string;
  primaDeAntiguedad: boolean;
  primaPorJubilacion: boolean;
  seguroDeVida: boolean;
  pagoDeBeneficiosIndividualesForemex: boolean;
  comentarios: string;
  cveKdm1: number;

  antiguedadConcepto: string;
  antiguedadFechaInicio: string;
  antiguedadFechaFin: string;
  antiguedadAnios: string;
  antiguedadMeses: string;
  antiguedadEfectiva: string;
  totalAniosCumplidos: string;
  importeConSueldoBase: string;
  importeConSalarioMinimo: string;
  importePagar: string;
};

export type RawDavsTramiteInfo = {
  unidadAdministrativa?: string | null;
  folio?: string | null;
  folioAsesoria?: string | null;
  fecha?: string | null;
  fechaBaja?: string | null;
  horaInicial?: string | null;
  horaFinal?: string | null;
  csp?: string | null;
  nombre?: string | null;
  rfc?: string | null;
  issemym?: string | null;
  direccion?: string | null;
  telefonoOficina?: string | null;
  telefonoParticular?: string | null;
  lugarDePago?: string | null;
  tipoDeSindicato?: string | null;
  motivoSeparacion?: string | null;
  tipoSeguro?: string | null;
  sueldoBase?: string | null;
  quincena?: string | null;
  subsecretaria?: string | null;
  direccionGeneral?: string | null;
  direccionDeArea?: string | null;
  subdireccion?: string | null;
  departamento?: string | null;
  puesto?: string | null;
  nivelRango?: string | null;
  sueldoBaseMensual?: string | null;
  primaDeAntiguedad?: string | number | null;
  primaPorJubilacion?: string | number | null;
  seguroDeVida?: string | number | null;
  pagoDeBeneficiosIndividualesForemex?: string | number | null;
  comentarios?: string | null;
  cveKdm1?: number | null;
};

export type RawDavsTramiteFecha = {
  concepto?: string | null;
  diaInicial?: string | number | null;
  mesInicial?: string | number | null;
  anioInicial?: string | number | null;
  diaFinal?: string | number | null;
  mesFinal?: string | number | null;
  anioFinal?: string | number | null;
  aniosTotales?: number | null;
  mesesTotales?: number | null;
};

export type RawDavsTramiteAntiguedad = {
  aniosEfectivos?: number | null;
  mesesEfectivos?: number | null;
  totalAniosCumplidos?: number | null;
  importeSueldoBase?: number | null;
  importeSalarioMinimo?: number | null;
  importeAPagar?: number | null;
};

export type RawDavsTramite = {
  tramite?: RawDavsTramiteInfo | null;
  fecha?: RawDavsTramiteFecha | null;
  antiguedad?: RawDavsTramiteAntiguedad | null;
};

function normalizeFlag(value?: string | number | null): boolean {
  const v = String(value ?? '').trim();
  return v === '1' || v === 'true' || v === '5';
}

function formatMoney(value?: number | null): string {
  if (value == null || Number.isNaN(value)) return '';

  return new Intl.NumberFormat('es-MX', {
    style: 'currency',
    currency: 'MXN',
    minimumFractionDigits: 2,
  }).format(value);
}

function buildFechaCompleta(
  dia?: string | number | null,
  mes?: string | number | null,
  anio?: string | number | null
): string {
  const d = String(dia ?? '').trim();
  const m = String(mes ?? '').trim();
  const a = String(anio ?? '').trim();

  if (!d && !m && !a) return '';
  return `${d || '—'}/${m || '—'}/${a || '—'}`;
}

function buildAntiguedadEfectiva(
  anios?: number | null,
  meses?: number | null
): string {
  const years = anios ?? 0;
  const months = meses ?? 0;
  return `${years} año(s) ${months} mes(es)`;
}

export const normalizeDavsTramite = (
  raw: RawDavsTramite,
  params: RequestDavsTramite
): DavsTramiteResponse => {
  const tramite = raw?.tramite;
  const fecha = raw?.fecha;
  const antiguedad = raw?.antiguedad;

  return {
    unidadAdministrativa: tramite?.unidadAdministrativa ?? '',
    folio: tramite?.folio ?? '',
    folioAsesoria: tramite?.folioAsesoria ?? '',
    fecha: tramite?.fecha ?? '',
    fechaBaja: tramite?.fechaBaja ?? '',
    horaInicial: tramite?.horaInicial ?? '',
    horaFinal: tramite?.horaFinal ?? '',
    claveServidor: tramite?.csp ?? '',
    nombre: tramite?.nombre ?? '',
    rfc: tramite?.rfc ?? '',
    issemym: tramite?.issemym ?? '',
    direccion: tramite?.direccion ?? '',
    telefonoOficina: tramite?.telefonoOficina ?? '',
    telefonoParticular: tramite?.telefonoParticular ?? '',
    lugarDePago: tramite?.lugarDePago ?? '',
    tipoDeSindicato: tramite?.tipoDeSindicato ?? '',
    motivoSeparacion: tramite?.motivoSeparacion ?? '',
    tipoSeguro: tramite?.tipoSeguro ?? '',
    sueldoBase: tramite?.sueldoBase ?? '',
    quincena: tramite?.quincena ?? '',
    subsecretaria: tramite?.subsecretaria ?? '',
    direccionGeneral: tramite?.direccionGeneral ?? '',
    direccionDeArea: tramite?.direccionDeArea ?? '',
    subdireccion: tramite?.subdireccion ?? '',
    departamento: tramite?.departamento ?? '',
    puesto: tramite?.puesto ?? '',
    nivelRango: tramite?.nivelRango ?? '',
    sueldoBaseMensual: tramite?.sueldoBaseMensual ?? '',
    primaDeAntiguedad: normalizeFlag(tramite?.primaDeAntiguedad),
    primaPorJubilacion: normalizeFlag(tramite?.primaPorJubilacion),
    seguroDeVida: normalizeFlag(tramite?.seguroDeVida),
    pagoDeBeneficiosIndividualesForemex: normalizeFlag(
      tramite?.pagoDeBeneficiosIndividualesForemex
    ),
    comentarios: tramite?.comentarios ?? '',
    cveKdm1: tramite?.cveKdm1 ?? params.cveKdm1,

    antiguedadConcepto: fecha?.concepto ?? '',
    antiguedadFechaInicio: buildFechaCompleta(
      fecha?.diaInicial,
      fecha?.mesInicial,
      fecha?.anioInicial
    ),
    antiguedadFechaFin: buildFechaCompleta(
      fecha?.diaFinal,
      fecha?.mesFinal,
      fecha?.anioFinal
    ),
    antiguedadAnios: String(fecha?.aniosTotales ?? ''),
    antiguedadMeses: String(fecha?.mesesTotales ?? ''),
    antiguedadEfectiva: buildAntiguedadEfectiva(
      antiguedad?.aniosEfectivos,
      antiguedad?.mesesEfectivos
    ),
    totalAniosCumplidos: String(antiguedad?.totalAniosCumplidos ?? ''),
    importeConSueldoBase: formatMoney(antiguedad?.importeSueldoBase),
    importeConSalarioMinimo: formatMoney(antiguedad?.importeSalarioMinimo),
    importePagar: formatMoney(antiguedad?.importeAPagar),
  };
};