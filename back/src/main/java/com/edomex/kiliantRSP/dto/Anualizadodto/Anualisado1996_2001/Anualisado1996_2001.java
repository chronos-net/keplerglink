package com.edomex.kiliantRSP.dto.Anualizadodto.Anualisado1996_2001;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.*;
import com.edomex.kiliantRSP.service.impl.AnualisadoImpl.Anualisado;

import java.util.List;

public record Anualisado1996_2001(
        AnualisadoEmpleadoDto empleado,
        ResumenGlobalDto resumenGlobal,
        CatalogosDto catalogos,
        List<PlazasAnualisadosDto> plazas,
        List<PeriodoDto> periodos
)
implements Anualisado {}
