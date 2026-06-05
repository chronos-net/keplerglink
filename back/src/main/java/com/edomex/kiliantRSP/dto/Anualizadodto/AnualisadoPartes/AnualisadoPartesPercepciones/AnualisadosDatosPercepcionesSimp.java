package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoPartesPercepciones;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnualisadosDatosPercepcionesSimp(
        BigDecimal total,
        List<AnualisadoPercepcionesAnti> percepciones
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record AnualisadoPercepcionesAnti(
            List<PercepcionesClave> percepciones_clave,
            List<PercepcionesImporte> percepciones_imp
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PercepcionesClave(
            String pern
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PercepcionesImporte(
            String periodo,
            Map<String, BigDecimal> importes, // "imp1" -> 10.13, "imp2" -> 87.62
            BigDecimal total
    ) {}

}
