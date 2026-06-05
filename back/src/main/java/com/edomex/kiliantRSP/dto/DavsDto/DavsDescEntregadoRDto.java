package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;

public record DavsDescEntregadoRDto(
        String unidadAdministrativa,
        Integer tipo,
        String importeII,
        String movimiento,
        String folio,
        String fecha,
        String horaInicio,
        String horaFinal,
        String claveServidor,
        String enFavorDe,
        String rfc,
        BigDecimal importe,
        String mensajeCompleto,
        Long cveKdm1
) {
}
