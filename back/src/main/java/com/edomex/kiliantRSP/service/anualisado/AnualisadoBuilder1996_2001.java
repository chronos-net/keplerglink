package com.edomex.kiliantRSP.service.anualisado;

import com.edomex.kiliantRSP.dto.Anualizadodto.Anualisado1996_2001.Anualisado1996_2001;
import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.Anualiasadodto;
import com.edomex.kiliantRSP.service.AnualisadoService.*;
import org.springframework.stereotype.Component;

@Component
public class AnualisadoBuilder1996_2001 implements AnualisadoBuild<Anualisado1996_2001>{

    private final AnualisadoEmpleadoService     anualisadoEmpleadoService;
    private final ResumenGlobalService          resumenGlobalService;
    private final CatalogosAnulisadosService    catalogosAnulisadosService;
    private final PlazasAnualisadoService       plazasAnualisadoService;
    private final PeriodosAnualisadoService     periodosAnualisadoService;


    public AnualisadoBuilder1996_2001(
            AnualisadoEmpleadoService   anualisadoEmpleadoService,
            ResumenGlobalService        resumenGlobalService,
            CatalogosAnulisadosService  catalogosAnulisadosService,
            PlazasAnualisadoService     plazasAnualisadoService,
            PeriodosAnualisadoService   periodosAnualisadoService

    ){
        this.anualisadoEmpleadoService  = anualisadoEmpleadoService;
        this.resumenGlobalService       = resumenGlobalService;
        this.catalogosAnulisadosService = catalogosAnulisadosService;
        this.plazasAnualisadoService    = plazasAnualisadoService;
        this.periodosAnualisadoService = periodosAnualisadoService;
    }

    @Override
    public Anualisado1996_2001 build(Anualiasadodto dto){

        var empleado = anualisadoEmpleadoService.obtenerEmpleado(dto.neyemp(), dto.anio());
        var totalesGlobales = resumenGlobalService.obtenerResumenGlobal(dto.neyemp(), dto.anio());
        var catalogosAnualisados = catalogosAnulisadosService.obtenerCatalogosAnualisado(dto.neyemp(), dto.anio());
        var plazasAnualisados = plazasAnualisadoService.obtenerPlazasAnalisados(dto.neyemp(), dto.anio());
        var peridosAnualisados = periodosAnualisadoService.obtenerPeridosAnualisado(dto.neyemp(), dto.anio());

        return new Anualisado1996_2001(
                empleado,
                totalesGlobales,
                catalogosAnualisados,
                plazasAnualisados,
                peridosAnualisados
        );
    }
}
