package com.edomex.kiliantRSP.service.descPlazas;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DescPlazasdto;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazas2002_2022.DescPlazas2002_2022;
import com.edomex.kiliantRSP.service.DescPlazasService.*;
import org.springframework.stereotype.Component;

@Component
public class DescPlazasBuilder2002_2022 implements DescPlazasBuild<DescPlazas2002_2022> {

    private final PlazasEncabesadoService2002       plazasEncabesadoService2002;
    private final PlazaPrincipalService1996         plazaPrincipalService1996;
    private final PlazaTotalesGlobalesservice1996   plazaTotalesGlobalesservice1996;
    private final Plazas2002Service                 plazas2002Service;

    public DescPlazasBuilder2002_2022(
            PlazasEncabesadoService2002       plazasEncabesadoService2002,
            PlazaPrincipalService1996         plazaPrincipalService1996,
            PlazaTotalesGlobalesservice1996   plazaTotalesGlobalesservice1996,
            Plazas2002Service                 plazas2002Service
    ){
        this.plazas2002Service = plazas2002Service;
        this.plazaPrincipalService1996 = plazaPrincipalService1996;
        this.plazaTotalesGlobalesservice1996 = plazaTotalesGlobalesservice1996;
        this.plazasEncabesadoService2002 = plazasEncabesadoService2002;
    }

    @Override
    public DescPlazas2002_2022 build(DescPlazasdto dto){
        var encabesadoPlaza = plazasEncabesadoService2002.obtenerEncabezadoPlazas(dto.neyemp(), dto.anio(), dto.quincena());
        var principalPlaza = plazaPrincipalService1996.obtenerPlazaPrincipal(dto.neyemp(), dto.anio(), dto.quincena());
        var totalGlobales = plazaTotalesGlobalesservice1996.obtenerTotalGlobal(dto.neyemp(), dto.anio(), dto.quincena());
        var plazasService = plazas2002Service.obtenerDesglosePlazas(dto.neyemp(), dto.anio(), dto.quincena());

        return new DescPlazas2002_2022(
                encabesadoPlaza,
                principalPlaza,
                totalGlobales,
                plazasService
        );
    }
}
