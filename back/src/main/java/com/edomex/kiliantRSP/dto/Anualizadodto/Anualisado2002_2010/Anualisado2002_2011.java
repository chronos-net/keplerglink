package com.edomex.kiliantRSP.dto.Anualizadodto.Anualisado2002_2010;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.*;
;
import com.edomex.kiliantRSP.service.impl.AnualisadoImpl.Anualisado;

import java.util.List;

public record Anualisado2002_2011(
        AnualisadoEmpleadoDto empleado,
        ResumenGlobalDto resumenGlobal,
        CatalogosDto catalogos,
        List<PlazasAnualisadosDto> plazas,
        List<PeriodoDto> periodos

) implements Anualisado {
}
