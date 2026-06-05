package com.edomex.kiliantRSP.service.BuscadorHistoricoGlinkService;

import com.edomex.kiliantRSP.dto.BuscadorHistoricoGlinkDto.BuscadorHistoricoGlinkdto;

import java.util.List;

public interface BuscadorHistoricoGlinkService {
    List<BuscadorHistoricoGlinkdto> buscadorPorNeyemp(String cvesp);
}
