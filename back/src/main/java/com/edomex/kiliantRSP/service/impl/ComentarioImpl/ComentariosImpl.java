package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioValoresDto;
import com.edomex.kiliantRSP.dto.ComentarioDto.ComentariosDto;
import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioReciboCompleto;
import com.edomex.kiliantRSP.service.ComentarioService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComentariosImpl implements ComentariosService{

    private final ComentarioCabeseraService comentarioCabeseraService;
    private final ComentarioValoresService comentarioValoresService;

    @Override
    public ComentarioReciboCompleto obtenerComentarios(ComentariosDto request){

        String neyemp = request.neyemp();

        var cabesera = comentarioCabeseraService.obtenerCabesera(neyemp);
        var valores = comentarioValoresService.obtenerValoresService(neyemp);


        return new ComentarioReciboCompleto(
                cabesera,
                valores
        );
    }
}
