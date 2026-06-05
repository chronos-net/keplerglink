package com.edomex.kiliantRSP.service.ComentarioService;

import com.edomex.kiliantRSP.dto.ComentarioDto.CuerpoComentarioDto;

import java.util.List;

public interface CuerpoComentarioService {

    List<CuerpoComentarioDto>  obtenerCuerpo(String neyemp);
}
