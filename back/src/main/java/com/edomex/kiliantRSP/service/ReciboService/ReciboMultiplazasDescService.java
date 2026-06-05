package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboMultiPlazasDto;

import java.util.List;

public interface ReciboMultiplazasDescService {

    List<ReciboMultiPlazasDto> obtenerMultiplazas(String neyemp, int anio, String quincena);
}
