package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes;

import java.util.List;

public record PeriodoDto(
        String periodo,
        String plazaRef,
        String estatus,
        ResumenPeriodoDto resumen,
        List<MovimientoDto> percepciones,
        List<MovimientoDto> deducciones
) {}
