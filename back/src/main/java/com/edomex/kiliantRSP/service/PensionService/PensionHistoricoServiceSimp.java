package com.edomex.kiliantRSP.service.PensionService;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionSimp;

public interface PensionHistoricoServiceSimp {
    PensionSimp obtenerPensionSimp(String neyemp, String periodo, int anio);
}
