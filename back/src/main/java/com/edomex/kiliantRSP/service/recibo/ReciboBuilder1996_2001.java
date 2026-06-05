package com.edomex.kiliantRSP.service.recibo;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboRegresoDto;
import com.edomex.kiliantRSP.dto.Recibodto.Recibodto;
import com.edomex.kiliantRSP.service.ReciboService.*;
import org.springframework.stereotype.Component;

@Component("builder1996")
public class ReciboBuilder1996_2001 implements ReciboBuild<ReciboRegresoDto> {

    private final ReciboEmpleadoService     reciboEmpleadoService;
    private final ReciboNumeroService       reciboNumeroService;
    private final ReciboPlazaService        reciboPlazaService;
    private final ReciboTotalService        reciboTotalService;
    private final ReciboConceptoService     reciboConceptoService;
    private final ReciboMultiPlazasService  reciboMultiPlazasService;

    public ReciboBuilder1996_2001(
            ReciboEmpleadoService   reciboEmpleadoService,
            ReciboNumeroService     reciboNumeroService,
            ReciboPlazaService      reciboPlazaService,
            ReciboTotalService      reciboTotalService,
            ReciboConceptoService   reciboConceptoService,
            ReciboMultiPlazasService   reciboMultiPlazasService

    ){
        this.reciboEmpleadoService      = reciboEmpleadoService;
        this.reciboNumeroService        = reciboNumeroService;
        this.reciboPlazaService         = reciboPlazaService;
        this.reciboTotalService         = reciboTotalService;
        this.reciboConceptoService      = reciboConceptoService;
        this.reciboMultiPlazasService   = reciboMultiPlazasService;
    }

    @Override
    public ReciboRegresoDto build(Recibodto dto){

        var empelado        = reciboEmpleadoService.obtenerReciboEmpelado(dto.neyemp(), dto.anio(), dto.quincena());
        var numero          = reciboNumeroService.obtenerNumero(dto.neyemp(), dto.anio(),dto.quincena());
        var plaza           = reciboPlazaService.obtenerPlaza(dto.neyemp(), dto.anio(),dto.quincena());
        var total           = reciboTotalService.obtenerTotal(dto.neyemp(), dto.anio(), dto.quincena());
        var percepciones    = reciboConceptoService.obtenerPercepciones(dto.neyemp(), dto.anio(),dto.quincena());
        var deducciones     = reciboConceptoService.obtenerDeducciones(dto.neyemp(), dto.anio(), dto.quincena());
        var multiplazas     = reciboMultiPlazasService.obtenerMultiPlazas(dto.neyemp(), dto.anio(), dto.quincena());

        return new ReciboRegresoDto(
                empelado,
                numero,
                plaza,
                total,
                percepciones,
                deducciones,
                multiplazas
        );
    }
}
