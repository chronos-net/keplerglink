package com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioValoresDto;

import java.util.List;


public interface ComentarioReciboValoresService {

    List<ComentarioValoresDto> obtenerValoresService(String neyemp, String periodo, int anio);
}
