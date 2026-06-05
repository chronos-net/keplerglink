package com.edomex.kiliantRSP.service.DavsService;

import com.edomex.kiliantRSP.dto.DavsDto.DavsEntregadoDto;

import java.util.List;

public interface DavsHentrega {

    List<DavsEntregadoDto> obtenerEntrega(String neyemp, String negnom);
}
