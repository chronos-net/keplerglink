export type DescPlazasRequest = {
  quincena: string
  anio: number
  nombreSp: string
  neyemp: string
}

export type DescPlazasResponseDto = {
  empleado: {
    neyemp: string
    negnom: string
    rfc: string
    cheque: string
    ads: string
    banco?: string
    numCuenta?: string
    numRecibo?: string
    lugPago?: string
  }
  plazaPrincipal: {
    plazaId: string
    secuenciaPlaza: string
  }
  totalGlobales: {
    percepciones: number
    deducciones: number
    neto: number
  }
  plazas: Array<{
    plazaId: string
    secuenciaPlaza: string
    puesto: string
    leyendaPuesto: string
    lugpago: string
    centroTrabajo: string
    percepciones: {
      total: number
      percepciones: Array<{ codigo: string; importe: number }>
    }
    deducciones: {
      total: number
      deducciones: Array<{ codigo: string; importe: number }>
    }
    neto: number
  }>
}
