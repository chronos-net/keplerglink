package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboEmleadoDto;

public interface ReciboEmpleadoService {

    ReciboEmleadoDto obtenerReciboEmpelado(String neyemp, int anio, String quincena);
}
