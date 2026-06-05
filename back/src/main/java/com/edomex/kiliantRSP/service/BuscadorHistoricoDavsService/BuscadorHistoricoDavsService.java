package com.edomex.kiliantRSP.service.BuscadorHistoricoDavsService;

import com.edomex.kiliantRSP.dto.BuscadorHistoricoDavsDto.BuscadorHistoricoDavsDto;

import java.util.List;

public interface BuscadorHistoricoDavsService {

    List<BuscadorHistoricoDavsDto> buscar(String termino);
}
