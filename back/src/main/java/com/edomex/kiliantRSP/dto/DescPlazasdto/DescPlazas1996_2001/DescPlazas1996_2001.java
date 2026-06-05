package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazas1996_2001;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.*;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.DescPlazas;

import java.util.List;

public record DescPlazas1996_2001(
        DesglosePlazasEncabesado1996Dto             empleado,
        PlazaPrincipal1996Dto                       plazaPrincipal,
        TotalGlobales1996Dto                        totalGlobales,
        List<PlazasDto1996>                         plazas
) implements DescPlazas {
}
