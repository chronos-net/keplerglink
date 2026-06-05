package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes;

import java.math.BigDecimal;

public record MovimientoDto(
        String codigo,
        BigDecimal importe
) {}
