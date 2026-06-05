package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoPartesPercepciones;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnualisadoDatosPercepcionesDesc(
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
            String pern,
            String desc
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PercepcionesImporte(
            String periodo,
            Map<String, BigDecimal> importes,
            BigDecimal total
    ) {}
}
