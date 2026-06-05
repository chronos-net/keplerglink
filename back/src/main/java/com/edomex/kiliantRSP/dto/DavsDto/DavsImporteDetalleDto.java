package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;

public record DavsImporteDetalleDto(

        int aniosEfectivos,
        int mesesEfectivos,
        int totalAniosCumplidos,

        BigDecimal importeSueldoBase,
        BigDecimal importeSalarioMinimo,
        BigDecimal importeAPagar

) {}