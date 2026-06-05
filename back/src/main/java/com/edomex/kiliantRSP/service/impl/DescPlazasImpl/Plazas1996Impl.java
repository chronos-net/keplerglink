package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.PlazasDto1996;
import com.edomex.kiliantRSP.service.DescPlazasService.*;
import com.edomex.kiliantRSP.service.descPlazas.builders.Plazas1996Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Plazas1996Impl implements Plazas1996Service {

    private final PlazasBase1996Service plazasBase1996Service;
    private final Percepciones1996Service percepciones1996Service;
    private final Deducciones1996Service deducciones1996Service;
    private final Plazas1996Builder plazas1996Builder;

    @Override
    public List<PlazasDto1996> obtenerDesglosePlazas(
            String neyemp,
            int anio,
            String quincena
    ) {

        var plazaBase = plazasBase1996Service.obtenerPlazasBase(neyemp, anio, quincena);
        var percepciones = percepciones1996Service.obtenerPercepciones(neyemp, anio, quincena);
        var deducciones = deducciones1996Service.obtenerDeducciones(neyemp, anio, quincena);

        return plazas1996Builder.build(
                plazaBase,
                percepciones,
                deducciones
        );
    }
}


