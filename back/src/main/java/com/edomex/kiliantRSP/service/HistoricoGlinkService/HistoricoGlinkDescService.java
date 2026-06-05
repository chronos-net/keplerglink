package com.edomex.kiliantRSP.service.HistoricoGlinkService;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkDesc;

import java.util.List;

public interface HistoricoGlinkDescService {
    List<HistoricoGlinkDesc> obtenerDescripciones(String neyemp);
}
