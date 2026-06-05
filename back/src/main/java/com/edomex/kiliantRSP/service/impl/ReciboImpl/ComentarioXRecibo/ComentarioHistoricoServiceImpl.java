package com.edomex.kiliantRSP.service.impl.ReciboImpl.ComentarioXRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioReciboCompleto;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboCabeseraService;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboService;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboValoresService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComentarioHistoricoServiceImpl implements ComentarioReciboService {

    private final ComentarioReciboCabeseraService comentarioReciboCabeseraService;
    private final ComentarioReciboValoresService comentarioReciboValoresService;

    @Override
    public ComentarioReciboCompleto obtenerDatosComentarios(String neyemp, String periodo, int anio) {

        var cabesera = comentarioReciboCabeseraService.obtenerCabeseraService(neyemp, periodo, anio);
        var valores = comentarioReciboValoresService.obtenerValoresService(neyemp, periodo, anio);

        return new ComentarioReciboCompleto(
                cabesera,
                valores
        );
    }
}
