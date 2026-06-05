package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.Davsdto;
import com.edomex.kiliantRSP.dto.DavsDto.DavsHistoricoDto;
import com.edomex.kiliantRSP.service.DavsService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DavsImpl implements DavsService {


    private final DavsHasesoria     davsHasesoria;
    private final DavsHtramite      davsHtramite;
    private final DavsHsolicitud    davsHsolicitud;
    private final DavsHentrega      davsHentrega;

    @Override
    public Davs getDavs(Davsdto dto) {

        var asesoria = davsHasesoria.obtenerAsesoria(dto.neyemp(), dto.negnom());
        var tramite  = davsHtramite.obtenerTramite(dto.neyemp(), dto.negnom());
        var solicitud= davsHsolicitud.obtenerSolicitud(dto.neyemp(), dto.negnom());
        var entrega  = davsHentrega.obtenerEntrega(dto.neyemp(), dto.negnom());

        return new DavsResponse(
                asesoria,
                tramite,
                solicitud,
                entrega
        );
    }

}
