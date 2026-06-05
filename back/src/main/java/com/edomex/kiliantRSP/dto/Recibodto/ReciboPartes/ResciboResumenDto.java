package com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes;

import java.math.BigDecimal;

public record ResciboResumenDto(
        BigDecimal totalPercepciones,
        BigDecimal totalDeducciones,
        BigDecimal neto
) {
}
