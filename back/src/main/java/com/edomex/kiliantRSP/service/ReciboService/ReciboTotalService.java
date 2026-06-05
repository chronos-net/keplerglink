package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ResciboResumenDto;

public interface ReciboTotalService {

    ResciboResumenDto obtenerTotal(String neyemp, int anio, String quincena);
}
