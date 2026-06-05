package com.edomex.kiliantRSP.service.ComentarioService;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioReciboCompleto;
import com.edomex.kiliantRSP.dto.ComentarioDto.ComentariosDto;

public interface ComentariosService {
    ComentarioReciboCompleto obtenerComentarios(ComentariosDto request);
}