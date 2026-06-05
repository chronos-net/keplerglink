package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazas2002_2022;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.*;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.DescPlazas;

import java.util.List;

public record DescPlazas2002_2022(
        DesglosePlazasEncabesado2002Dto empleado,
        PlazaPrincipal1996Dto           plazaPrincipal,
        TotalGlobales1996Dto            totalGlobales,
        List<PlazasDto1996>             plazas
        ) implements DescPlazas {
}
