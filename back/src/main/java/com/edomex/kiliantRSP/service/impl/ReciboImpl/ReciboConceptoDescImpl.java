package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboConceptoDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboConceptoDescService;
import com.edomex.kiliantRSP.service.helpers.Recibos.ReciboCatalogoHelper;
import com.edomex.kiliantRSP.service.helpers.Recibos.ReciboMovimientosHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReciboConceptoDescImpl implements ReciboConceptoDescService {

    private final ReciboMovimientosHelper movimientosHelper;
    private final ReciboCatalogoHelper catalogoHelper;

    @Override
    public List<ReciboConceptoDto> obtenerPercepcionesDesc(
            String neyemp,
            int anio,
            String quincena
    ) {

        List<ReciboConceptoDto> base = movimientosHelper.obtenerMovimientos(
                "vista_percepciones_" + anio,
                neyemp,
                quincena,
                "per"
        );

        return catalogoHelper.enriquecerConDescripcion(base, anio);
    }

    @Override
    public List<ReciboConceptoDto> obtenerDeduccionesDesc(
            String neyemp,
            int anio,
            String quincena
    ) {

        List<ReciboConceptoDto> base = movimientosHelper.obtenerMovimientos(
                "vista_deducciones_" + anio,
                neyemp,
                quincena,
                "ded"
        );

        return catalogoHelper.enriquecerConDescripcion(base, anio);
    }
}