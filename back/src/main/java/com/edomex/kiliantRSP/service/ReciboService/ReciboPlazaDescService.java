package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboPlazaDto;

public interface ReciboPlazaDescService {

    ReciboPlazaDto obtenerPlaza(String neyemp, int anio, String quincena);
}
