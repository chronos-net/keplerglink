package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DescPlazasDatosDeduccionesSimp(
        String secuenciaPlaza,
        BigDecimal total,
        List<DescPlazasDatosDeduccionesAnti> deducciones
) {
}
