package com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioReciboCompleto;

public interface ComentarioReciboService {
    ComentarioReciboCompleto obtenerDatosComentarios(String neyemp, String periodo, int anio);

}
