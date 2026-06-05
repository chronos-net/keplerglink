package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoPartesDeducciones;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnualisadoDatosDeduccionesSimp(
        BigDecimal total,
        List<AnualisadoDeduccionesAnti> deducciones
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record AnualisadoDeduccionesAnti(
            List<DeduccionesClave> deducciones_clave,
            List<DeduccionesImporte> deducciones_imp
    ){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record DeduccionesClave(
            String ded
    ){}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record DeduccionesImporte(
            String periodo,
            Map<String, BigDecimal> importes,
            BigDecimal total
    ){}
}
