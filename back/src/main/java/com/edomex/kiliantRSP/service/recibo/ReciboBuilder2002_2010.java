package com.edomex.kiliantRSP.service.recibo;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboRegresoDto;
import com.edomex.kiliantRSP.dto.Recibodto.Recibodto;
import com.edomex.kiliantRSP.service.ReciboService.*;
import org.springframework.stereotype.Component;

@Component("builder2002")
public class ReciboBuilder2002_2010 implements ReciboBuild<ReciboRegresoDto> {

    private final ReciboEmpleadoService     reciboEmpleadoService;
    private final ReciboNumeroService       reciboNumeroService;
    private final ReciboPlazaDescService    reciboPlazaDescService;
    private final ReciboTotalService        reciboTotalService;
    private final ReciboConceptoDescService reciboConceptoDescService;
    private final ReciboMultiplazasDescService reciboMultiplazasDescService;

    public ReciboBuilder2002_2010(
            ReciboEmpleadoService       reciboEmpleadoService,
            ReciboNumeroService         reciboNumeroService,
            ReciboPlazaDescService      reciboPlazaDescService,
            ReciboTotalService          reciboTotalService,
            ReciboConceptoDescService   reciboConceptoDescService,
            ReciboMultiplazasDescService reciboMultiplazasDescService
    ){
        this.reciboEmpleadoService      = reciboEmpleadoService;
        this.reciboNumeroService        = reciboNumeroService;
        this.reciboPlazaDescService     = reciboPlazaDescService;
        this.reciboTotalService         = reciboTotalService;
        this.reciboConceptoDescService  = reciboConceptoDescService;
        this.reciboMultiplazasDescService = reciboMultiplazasDescService;
    }

    @Override
    public ReciboRegresoDto build(Recibodto dto) {
        var empelado        = reciboEmpleadoService.obtenerReciboEmpelado(dto.neyemp(), dto.anio(), dto.quincena());
        var numero          = reciboNumeroService.obtenerNumero(dto.neyemp(), dto.anio(),dto.quincena());
        var plaza           = reciboPlazaDescService.obtenerPlaza(dto.neyemp(), dto.anio(), dto.quincena());
        var total           = reciboTotalService.obtenerTotal(dto.neyemp(), dto.anio(), dto.quincena());
        var percepciones    = reciboConceptoDescService.obtenerPercepcionesDesc(dto.neyemp(), dto.anio(), dto.quincena());
        var deducciones     = reciboConceptoDescService.obtenerDeduccionesDesc(dto.neyemp(), dto.anio(), dto.quincena());
        var multiplazas     = reciboMultiplazasDescService.obtenerMultiplazas(dto.neyemp(), dto.anio(), dto.quincena());

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
