package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.PeriodoDto;
import com.edomex.kiliantRSP.service.AnualisadoService.PeriodosAnualisadoService;
import com.edomex.kiliantRSP.service.helpers.Anualisados.MovimientosHelper;
import com.edomex.kiliantRSP.service.helpers.Anualisados.PeriodosKddesHelper;
import com.edomex.kiliantRSP.service.helpers.Anualisados.ResumenHelper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodosAnualisadoimpl implements PeriodosAnualisadoService {

    private final MovimientosHelper movimientosHelper;
    private final ResumenHelper resumenHelper;
    private final PeriodosKddesHelper kddesHelper;

    @Override
    public List<PeriodoDto> obtenerPeridosAnualisado(String neyemp, int anio) {

        return obtenerCatalogoPeriodos().stream()
                .map(p -> construirPeriodo(neyemp, anio, p))
                .toList();
    }

    private List<String> obtenerCatalogoPeriodos() {
        return java.util.stream.IntStream.rangeClosed(1, 24)
                .mapToObj(i -> String.format("%02d", i))
                .toList();
    }

    private PeriodoDto construirPeriodo(
            String neyemp,
            int anio,
            String periodo
    ) {

        var registro = kddesHelper.obtenerUltimoRegistro(neyemp, anio, periodo);

        if (registro == null) {
            return new PeriodoDto(periodo, null, "SIN_PLAZA", null, List.of(), List.of());
        }

        var percepciones = movimientosHelper.obtenerPercepciones(neyemp, anio, periodo);
        var deducciones = movimientosHelper.obtenerDeducciones(neyemp, anio, periodo);
        var resumen = resumenHelper.construirResumen(percepciones, deducciones);

        return new PeriodoDto(
                periodo,
                "P" + registro.secuenciaPlaza(),
                "OK",
                resumen,
                percepciones,
                deducciones
        );
    }
}
