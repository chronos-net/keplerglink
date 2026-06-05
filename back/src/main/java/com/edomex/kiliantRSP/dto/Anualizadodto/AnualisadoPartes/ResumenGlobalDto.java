package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes;

import java.math.BigDecimal;

public record ResumenGlobalDto(
        Integer periodosConPago,
        Integer periodosSinPlaza,
        String primerPeriodo,
        String ultimoPeriodo,
        BigDecimal totalPercepciones,
        BigDecimal totalDeducciones,
        BigDecimal granTotalNeto
) {
}
