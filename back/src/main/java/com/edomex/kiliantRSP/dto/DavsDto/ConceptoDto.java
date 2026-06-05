package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;

public record ConceptoDto(

        String descripcion,
        BigDecimal importe,
        BigDecimal importe2

) {}