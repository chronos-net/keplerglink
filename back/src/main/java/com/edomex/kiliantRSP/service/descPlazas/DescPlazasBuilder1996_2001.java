package com.edomex.kiliantRSP.service.descPlazas;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DescPlazasdto;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazas1996_2001.DescPlazas1996_2001;
import com.edomex.kiliantRSP.service.DescPlazasService.*;
import org.springframework.stereotype.Component;

@Component
public class DescPlazasBuilder1996_2001 implements DescPlazasBuild<DescPlazas1996_2001> {

    /// instanciamos los servicios
    private final PlazasEncabesadoService1996       plazasEncabesadoService1996;
    private final PlazaPrincipalService1996         plazaPrincipalService1996;
    private final PlazaTotalesGlobalesservice1996   plazaTotalesGlobalesservice1996;
    private final Plazas1996Service                 plazas1996Service;


    public DescPlazasBuilder1996_2001(
            PlazasEncabesadoService1996         plazasEncabesadoService1996,
            PlazaPrincipalService1996           plazaPrincipalService1996,
            PlazaTotalesGlobalesservice1996     plazaTotalesGlobalesservice1996,
            Plazas1996Service                   plazas1996Service
    ){
        this.plazasEncabesadoService1996        = plazasEncabesadoService1996;
        this.plazaPrincipalService1996          = plazaPrincipalService1996;
        this.plazaTotalesGlobalesservice1996    = plazaTotalesGlobalesservice1996;
        this.plazas1996Service                  = plazas1996Service;
    }

    @Override
    public DescPlazas1996_2001 build(DescPlazasdto dto) {

        var encabesadoPlaza = plazasEncabesadoService1996.obtnerEncabesadoPlaza(dto.neyemp(), dto.anio(), dto.quincena());
        var principalPlaza = plazaPrincipalService1996.obtenerPlazaPrincipal(dto.neyemp(), dto.anio(), dto.quincena());
        var totalGlobales = plazaTotalesGlobalesservice1996.obtenerTotalGlobal(dto.neyemp(), dto.anio(), dto.quincena());
        var plazasService = plazas1996Service.obtenerDesglosePlazas(dto.neyemp(), dto.anio(), dto.quincena());

        return new DescPlazas1996_2001(
                encabesadoPlaza,
                principalPlaza,
                totalGlobales,
                plazasService
        );
    }
}
