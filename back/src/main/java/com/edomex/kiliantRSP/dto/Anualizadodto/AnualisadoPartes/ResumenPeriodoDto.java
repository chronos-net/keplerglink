package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes;

import java.math.BigDecimal;

public record ResumenPeriodoDto(
        BigDecimal percepciones,
        BigDecimal deducciones,
        BigDecimal neto
) {
}
