package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record DavsSolicitudFrontDto(

        String tipo,
        String subtipo,
        String folio,
        String estatus,

        Encabezado encabezado,
        ResumenPago resumenPago,
        Mensaje mensaje,

        List<ConceptoDto> conceptos,
        List<ConceptoDto> movimientosComplementarios

) {

    public record Encabezado(
            Date fecha,
            String rfc,
            String nombre
    ) {}

    public record ResumenPago(
            BigDecimal montoTotal,
            String montoTotalTexto
    ) {}

    public record Mensaje(
            String variant,
            String textoCompleto
    ) {}

}