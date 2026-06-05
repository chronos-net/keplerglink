package com.edomex.kiliantRSP.service.DavsService;

import com.edomex.kiliantRSP.dto.DavsDto.DavsAsesoriaDto;

import java.util.List;

public interface DavsHasesoria {

    /// metodo principal
    List<DavsAsesoriaDto> obtenerAsesoria(String neyemp, String negnom);
}
