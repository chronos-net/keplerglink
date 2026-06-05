package com.edomex.kiliantRSP.dto.PensionesGlinkDto;

public record PensionesGlinkResponse(

        String clavesp,
        String nombresp,
        String rfc,
        String fechain,
        String nombrepension,
        String tipo_desc,
        int altaqna,
        int altano,
        double porcentaje,
        double importe,
        String referencia
) {}


