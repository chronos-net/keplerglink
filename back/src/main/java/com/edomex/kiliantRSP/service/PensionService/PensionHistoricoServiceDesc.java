package com.edomex.kiliantRSP.service.PensionService;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionDesc;

public interface PensionHistoricoServiceDesc {
    PensionDesc obtenerPensionDesc(String neyemp, String periodo, int anio);
}
