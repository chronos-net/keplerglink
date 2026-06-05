package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboConceptoDto;

import java.util.List;

public interface ReciboConceptoService {

    List<ReciboConceptoDto> obtenerPercepciones(String neyemp, int anio, String quincena);

    List<ReciboConceptoDto> obtenerDeducciones(String neyemp, int anio, String quincena);
}
