package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboConceptoDto;

import java.util.List;

public interface ReciboConceptoDescService {

    List<ReciboConceptoDto> obtenerPercepcionesDesc(String neyemp, int anio, String quincena);

    List<ReciboConceptoDto> obtenerDeduccionesDesc(String neyemp, int anio, String quincena);
}
