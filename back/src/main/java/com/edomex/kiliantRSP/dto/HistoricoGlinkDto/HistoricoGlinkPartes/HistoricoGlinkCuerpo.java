package com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes;

import com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl.HistoricoGlink;
import java.util.List;

public record HistoricoGlinkCuerpo(
        HistoricoGlinkCabesera cabeseraHistorico,
        HistoricoGlinkPercepciones percepcionesHistorico,
        List<HistoricoGlinkDesc> descHistorico
) implements HistoricoGlink { }