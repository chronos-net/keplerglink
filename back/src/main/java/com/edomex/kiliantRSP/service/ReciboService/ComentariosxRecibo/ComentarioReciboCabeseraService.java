package com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioCabesera;

public interface ComentarioReciboCabeseraService {

    ComentarioCabesera obtenerCabeseraService(String neyemp, String periodo, int anio);
}
