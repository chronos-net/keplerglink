package com.edomex.kiliantRSP.dto.ComentarioDto;

import java.math.BigDecimal;

public record ComentarioValoresDto(
        String nombreTabla,
        Long id,
        String qna,
        String anio,
        String movimiento,
        String formaPago,
        String numCuenta,
        BigDecimal importeInicial,
        BigDecimal importeFinal,
        BigDecimal diferencia,
        String pensionado,
        String capturado,
        String comentario,
        String fechaCaptura,

        AdscripcionDto adscripcion,
        PuestoDto puesto,
        LugarPagoDto lugarPago
) {}
