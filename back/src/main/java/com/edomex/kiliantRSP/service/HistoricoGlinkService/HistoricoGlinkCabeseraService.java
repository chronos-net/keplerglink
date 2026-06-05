package com.edomex.kiliantRSP.service.HistoricoGlinkService;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkCabesera;

public interface HistoricoGlinkCabeseraService {
    HistoricoGlinkCabesera obtenerCabesera(String neyemp, String negnom, String rfc);
}
