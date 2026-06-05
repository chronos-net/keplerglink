export type RequestDevsAsesoria = {
    cveKdm1: number;
    neyemp: string;
}
export type DavsAseoriaItem = {
    primaAntiguedad: string;
    primaJubilacion: string;
    seguroVida: string;
    pagosIndividualesForemex: string;
}

export type DavsAsesoriaResponse = {
    unidadAdministrativa: string;
    folio: string;
    fecha: string;
    horaInicial: string;
    horaFinal: string;
    neyemp: string;
    nombre: string;
    rfc: string;
    issemym: string;
    direccion: string;
    telefonoOficina: string;
    telefonoParticular: string;
    lugarPago: string;
    sindicato: string;
    quincena: string;
    subSecretaria: string;
    direccionGeneral: string;
    direccionArea: string;
    subdireccion: string;
    departamento: string;
    puesto: string;
    nivelRango: string;
    sueldoMensual: string;
    comentarios: string;
    cveKdm1: number;
    tramitesSolicitados: DavsAseoriaItem;
};

export type RawDavsAsesoria = {
    unidadAdminitrativo?: string | null;
    folio?: string | null;
    fecha?: string | null;
    horaInicial?: string | null;
    horaFinal?: string | null;
    neyemp?: string | null;
    negnom?: string | null;
    rfc?: string | null;
    issemyn?: string | null;
    direccion?: string | null;
    telefonoOficina?: string | null;
    telefonoPArticular?: string | null;
    lugarPago?: string | null;
    sindicato?: string | null;
    quincena?: string | null;
    subSecretaria?: string | null;
    direccionGeneral?: string | null;
    direccionArea?: string | null;
    subdireccion?: string | null;
    departamento?: string | null;
    puesto?: string | null;
    nivelRango?: string | null;
    sueldoMensual?: string | null;
    primaAntiguedad?: string | null;
    primaJubilacio?: string | null;
    seguroVida?: string | null;
    pagosIndividualesForemex?: string | null;
    comentarios?: string | null;
    cveKdm1?: number | null;
}


export const normalizeDavsAsesoria = (
  raw: RawDavsAsesoria,
  params: RequestDevsAsesoria
): DavsAsesoriaResponse => {
  return {
    unidadAdministrativa: raw?.unidadAdminitrativo ?? "",
    folio: raw?.folio ?? "",
    fecha: raw?.fecha ?? "",
    horaInicial: raw?.horaInicial ?? "",
    horaFinal: raw?.horaFinal ?? "",
    neyemp: raw?.neyemp ?? params.neyemp ?? "",
    nombre: raw?.negnom ?? "",
    rfc: raw?.rfc ?? "",
    issemym: raw?.issemyn ?? "",
    direccion: raw?.direccion ?? "",
    telefonoOficina: raw?.telefonoOficina ?? "",
    telefonoParticular: raw?.telefonoPArticular ?? "",
    lugarPago: raw?.lugarPago ?? "",
    sindicato: raw?.sindicato ?? "",
    quincena: raw?.quincena ?? "",
    subSecretaria: raw?.subSecretaria ?? "",
    direccionGeneral: raw?.direccionGeneral ?? "",
    direccionArea: raw?.direccionArea ?? "",
    subdireccion: raw?.subdireccion ?? "",
    departamento: raw?.departamento ?? "",
    puesto: raw?.puesto ?? "",
    nivelRango: raw?.nivelRango ?? "",
    sueldoMensual: raw?.sueldoMensual ?? "",
    comentarios: raw?.comentarios ?? "",
    cveKdm1: raw?.cveKdm1 ?? params.cveKdm1,
    tramitesSolicitados: {
      primaAntiguedad: raw?.primaAntiguedad ?? "",
      primaJubilacion: raw?.primaJubilacio ?? "",
      seguroVida: raw?.seguroVida ?? "",
      pagosIndividualesForemex: raw?.pagosIndividualesForemex ?? "",
    },
  };
};;
