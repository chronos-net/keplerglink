package com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkdto;
import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkCuerpo;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkCabeseraService;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkDescService;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkPercepcionesService;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoGlinkServiceImpl implements HistoricoGlinkService {

    private final HistoricoGlinkCabeseraService cabeseraService;
    private final HistoricoGlinkPercepcionesService percepcionesService;
    private final HistoricoGlinkDescService descService;

    @Override
    public HistoricoGlink getVistaHistorico(HistoricoGlinkdto dto) {

        var cabecera = cabeseraService.obtenerCabesera(dto.neyemp(), dto.negnom(), dto.rfc());
        var percepciones = percepcionesService.obtenerPercepciones(dto.neyemp());
        var descripcion = descService.obtenerDescripciones(dto.neyemp());

        return new HistoricoGlinkCuerpo(
                cabecera,
                percepciones,
                descripcion
        );
    }
}
