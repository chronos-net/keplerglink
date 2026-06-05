package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;
import java.util.Date;

public record DavsDescSolicitudResponseDto(
        Date fecha,
        String rfc,
        String nombre,
        BigDecimal monto,
        Integer plazo,
        Date fechaPago,
        String comentario
) {}
