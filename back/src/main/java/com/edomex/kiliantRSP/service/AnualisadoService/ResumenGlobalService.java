package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.ResumenGlobalDto;

public interface ResumenGlobalService {

    ResumenGlobalDto obtenerResumenGlobal(String neyemp,  int anio);
}
