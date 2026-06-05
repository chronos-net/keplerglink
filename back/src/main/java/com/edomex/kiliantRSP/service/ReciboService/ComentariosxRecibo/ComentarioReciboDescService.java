package com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioDescDto;

import java.util.List;

public interface ComentarioReciboDescService {

    List<ComentarioDescDto>  obtenerDescService(String neyemp, String periodo, int anio);
}
