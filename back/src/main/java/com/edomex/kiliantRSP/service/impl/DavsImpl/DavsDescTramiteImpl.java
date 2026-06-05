package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDetalleCompletoDto;
import com.edomex.kiliantRSP.service.DavsService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DavsDescTramiteImpl implements DavsDescTramiteService {

    private final DavsTramiteDetalleService davsTramiteDetalleService;
    private final DavsFechaDetalleService davsFechaDetalleService;
    private final DavsAntiDetalleService davsAntiDetalleService;

    @Override
    public DavsDetalleCompletoDto obtenerDetalle(Long cveKdm1){

        var tramite = davsTramiteDetalleService.obtenerDetalleTramite(cveKdm1);
        var fecha = davsFechaDetalleService.obtenerFechasDetalle(cveKdm1);
        var antiguedad = davsAntiDetalleService.obtenerAntiDetalle(cveKdm1);

        return new DavsDetalleCompletoDto(
                tramite,
                fecha,
                antiguedad
        );
    }
}