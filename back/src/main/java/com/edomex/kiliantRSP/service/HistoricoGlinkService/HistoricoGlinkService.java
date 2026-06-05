package com.edomex.kiliantRSP.service.HistoricoGlinkService;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkdto;
import com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl.HistoricoGlink;

public interface HistoricoGlinkService {
    HistoricoGlink getVistaHistorico(HistoricoGlinkdto dto);
}