package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DescPlazasDatosPercepcionesSimp(
        String secuenciaPlaza,
        BigDecimal total,
        List<DescPlazasDatosPercepcionesAnti> percepciones
) {
}
