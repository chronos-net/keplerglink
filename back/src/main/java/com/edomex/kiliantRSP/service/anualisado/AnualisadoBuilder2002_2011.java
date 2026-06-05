package com.edomex.kiliantRSP.service.anualisado;

import com.edomex.kiliantRSP.dto.Anualizadodto.Anualisado2002_2010.Anualisado2002_2011;
import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.Anualiasadodto;
import com.edomex.kiliantRSP.service.AnualisadoService.*;
import org.springframework.stereotype.Component;

@Component
public class AnualisadoBuilder2002_2011 implements AnualisadoBuild<Anualisado2002_2011> {

    private final AnualisadoEmpleadoService anualisadoEmpleadoService;
    private final ResumenGlobalService resumenGlobalService;
    private final CatalogosAnulisadosDescService catalogosAnulisadosDescService;
    private final PlazasAnualisadoService plazasAnualisadoService;
    private final PeriodosAnualisadoService periodosAnualisadoService;

    public AnualisadoBuilder2002_2011(
            AnualisadoEmpleadoService anualisadoEmpleadoService,
            ResumenGlobalService resumenGlobalService,
            CatalogosAnulisadosDescService catalogosAnulisadosDescService,
            PlazasAnualisadoService plazasAnualisadoService,
            PeriodosAnualisadoService periodosAnualisadoService

    ){
        this.anualisadoEmpleadoService = anualisadoEmpleadoService;
        this.resumenGlobalService = resumenGlobalService;
        this.catalogosAnulisadosDescService = catalogosAnulisadosDescService;
        this.plazasAnualisadoService = plazasAnualisadoService;
        this.periodosAnualisadoService = periodosAnualisadoService;
    }

    @Override
    public Anualisado2002_2011 build(Anualiasadodto dto){

        var empleado = anualisadoEmpleadoService.obtenerEmpleado(dto.neyemp(), dto.anio());
        var totalesGlobales = resumenGlobalService.obtenerResumenGlobal(dto.neyemp(), dto.anio());
        var catalogosAnualisados = catalogosAnulisadosDescService.obtenerCatalogosAnualisados(dto.neyemp(), dto.anio());
        var plazasAnualisados = plazasAnualisadoService.obtenerPlazasAnalisados(dto.neyemp(), dto.anio());
        var peridosAnualisados = periodosAnualisadoService.obtenerPeridosAnualisado(dto.neyemp(), dto.anio());

        return new Anualisado2002_2011(
                empleado,
                totalesGlobales,
                catalogosAnualisados,
                plazasAnualisados,
                peridosAnualisados
        );
    }
}
