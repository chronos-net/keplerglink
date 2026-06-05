package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboConceptoDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboConceptoService;
import com.edomex.kiliantRSP.service.helpers.Recibos.ReciboMovimientosHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReciboConceptoImpl implements ReciboConceptoService {

    private final ReciboMovimientosHelper helper;

    @Override
    public List<ReciboConceptoDto> obtenerPercepciones(String neyemp, int anio, String quincena){
        return helper.obtenerMovimientos(
                "vista_percepciones_" + anio,
                neyemp,
                quincena,
                "per"
        );
    }

    @Override
    public List<ReciboConceptoDto> obtenerDeducciones(String neyemp, int anio, String quincena){
        return helper.obtenerMovimientos(
                "vista_deducciones_" + anio,
                neyemp,
                quincena,
                "ded"
        );
    }
}