package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoPartesPercepciones;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnualisadoPercepcionesAnti(
        List<PercepcionesClave> percepciones_clave,
        List<PercepcionesImporte> percepciones_imp
) {
    // Representa la clave de la percepción
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PercepcionesClave(String pern) {}

    // Representa el importe por periodo
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PercepcionesImporte(String periodo, BigDecimal impn) {}
}