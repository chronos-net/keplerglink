export type RequestDevsEntregados = {
    cveKdm1: number;
};


export type DavsEntregadoResponse = {
    unidadAdministrativa: string;
    tipo: number;
    importeII: string;
    movimiento: string;
    folio: string;
    fecha: string;
    horaInicio: string;
    horaFinal: string;
    claveServidor: string;
    enFavorDe: string;
    rfc: string;
    importe: number;
    mensajeCompleto: string;
    cveKdm1: number;
};


export type RawDavsEntregado = {
    unidadAdministrativa?: string | null;
    tipo?: number | null;
    importeII?: string | null;
    movimiento?: string | null;
    folio?: string | null;
    fecha?: string | null;
    horaInicio?: string | null;
    horaFinal?: string | null;
    claveServidor?: string | null;
    enFavorDe?: string | null;
    rfc?: string | null;
    importe?: number | null;
    mensajeCompleto?: string | null;
    cveKdm1?: number | null;
}

export const normalizeDavsEntregado = (
  raw: RawDavsEntregado,
  params: RequestDevsEntregados
): DavsEntregadoResponse => {
  return {
    unidadAdministrativa: raw?.unidadAdministrativa ?? '',
    tipo: raw?.tipo ?? 0,
    importeII: raw?.importeII ?? '',
    movimiento: raw?.movimiento ?? '',
    folio: raw?.folio ?? '',
    fecha: raw?.fecha ?? '',
    horaInicio: raw?.horaInicio ?? '',
    horaFinal: raw?.horaFinal ?? '',
    claveServidor: raw?.claveServidor ?? '',
    enFavorDe: raw?.enFavorDe ?? '',
    rfc: raw?.rfc ?? '',
    importe: raw?.importe ?? 0,
    mensajeCompleto: raw?.mensajeCompleto ?? '',
    cveKdm1: raw?.cveKdm1 ?? params.cveKdm1,
  };
};

