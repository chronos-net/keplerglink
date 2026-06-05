package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DescPlazasdto;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazas1996_2001.DescPlazas1996_2001;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazas2002_2022.DescPlazas2002_2022;
import com.edomex.kiliantRSP.service.DescPlazasService.DescPlazasService;
import com.edomex.kiliantRSP.service.descPlazas.DescPlazasBuild;
import org.springframework.stereotype.Service;

@Service
public class DescPlazasImpl implements DescPlazasService {

    /**
     * Implementación principal del servicio DescPlazasService.
     * Funciona como orquestador que selecciona el Builder adecuado según el año
     * recibido en el DTO de entrada, delegando la construcción del resultado final.
     */

    private final DescPlazasBuild<DescPlazas1996_2001> build1996_2001;
    private final DescPlazasBuild<DescPlazas2002_2022> build2002_2022;

    public  DescPlazasImpl(
            DescPlazasBuild<DescPlazas1996_2001> build1996_2001,
            DescPlazasBuild<DescPlazas2002_2022> build2002_2022
    ) {
        this.build1996_2001 = build1996_2001;
        this.build2002_2022 = build2002_2022;
    }

    @Override
    public DescPlazas getDescPlazas(DescPlazasdto dto){
        int anio = dto.anio();

        if(anio >= 1996 && anio <= 2001){
            return build1996_2001.build(dto);
        } else if (anio >= 2002 && anio <= 2022){
            return build2002_2022.build(dto);
        } else {
            throw new RuntimeException("No hay builder definido para el año " + anio);
        }
    }
}
