package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones;

import java.math.BigDecimal;
import java.util.List;

public record PercepcionesPlazaDto(
        String secuenciaPlaza,
        BigDecimal total,
        List<DescPlazasDatosPercepcionesAnti> percepciones
) {}
