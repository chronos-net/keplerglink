package com.edomex.kiliantRSP.service.impl.PrestamosGlinkImpl;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkdto;
import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkCuerpo;
import com.edomex.kiliantRSP.service.PrestamosGlinkService.PrestamosGlinkCabeseraService;
import com.edomex.kiliantRSP.service.PrestamosGlinkService.PrestamosGlinkDescService;
import com.edomex.kiliantRSP.service.PrestamosGlinkService.PrestamosGlinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrestamosGlinkServiceImpl implements PrestamosGlinkService {

    private final PrestamosGlinkCabeseraService prestamosGlinkCabeseraService;
    private final PrestamosGlinkDescService prestamosGlinkDescService;

    @Override
    public PrestamosGlinkCuerpo creacionPrestamoGlink(PrestamosGlinkdto dto){

        var cabesera = prestamosGlinkCabeseraService.obtenerCabesera(dto.neyemp());
        var descripcion = prestamosGlinkDescService.obtenerDescripciones(dto.neyemp());

        return new PrestamosGlinkCuerpo(
                cabesera,
                descripcion
        );
    }
}
