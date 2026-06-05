package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboMultiPlazasDto;

import java.util.List;

public interface ReciboMultiPlazasService {
    List<ReciboMultiPlazasDto> obtenerMultiPlazas(String neyemp, int anio, String quincena);
}
