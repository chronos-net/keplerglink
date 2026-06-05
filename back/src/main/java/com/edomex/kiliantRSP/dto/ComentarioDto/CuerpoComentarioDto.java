package com.edomex.kiliantRSP.dto.ComentarioDto;

import org.apache.commons.math3.analysis.function.Abs;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CuerpoComentarioDto(
        String periodo,
        String anio,
        String tipoPago,
        String cheque,
        BigDecimal importeInicial,
        String captura,
        String comentarioCompleto,
        String ads,
        String descripcion,
        String cat,
        String leyenda_puesto,
        String lug_pago,
        String car,
        String tipoRegegistro, //se llena aparte
        String tipo_movimento,
        String desc_movimiento,   // se llenan aparte
        String cuenta,
        BigDecimal inicial,
        BigDecimal costoFinal,
        String capturado_por,
        LocalDate fecha,
        String textComplementartio
) {
}
