package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.ResumenGlobalDto;
import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.ResumenPeriodoDto;
import com.edomex.kiliantRSP.service.AnualisadoService.ResumenGlobalService;
import com.edomex.kiliantRSP.service.helpers.Anualisados.MovimientosHelper;
import com.edomex.kiliantRSP.service.helpers.Anualisados.PeriodosKddesHelper;
import com.edomex.kiliantRSP.service.helpers.Anualisados.ResumenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumenGlobalImpl implements ResumenGlobalService {

    private final MovimientosHelper movimientosHelper;
    private final ResumenHelper resumenHelper;
    private final PeriodosKddesHelper periodosKddesHelper;

    @Override
    public ResumenGlobalDto obtenerResumenGlobal(String neyemp, int anio) {
        BigDecimal totalPercepciones = BigDecimal.ZERO;
        BigDecimal totalDeducciones = BigDecimal.ZERO;
        int periodosConPago = 0;
        List<String> periodosConMovimiento = new ArrayList<>();

        for (String periodo : obtenerCatalogoPeriodos()) {
            var registro = periodosKddesHelper.obtenerUltimoRegistro(neyemp, anio, periodo);

            if (registro == null) {
                continue;
            }

            var percepciones = movimientosHelper.obtenerPercepciones(neyemp, anio, periodo);
            var deducciones = movimientosHelper.obtenerDeducciones(neyemp, anio, periodo);
            var resumen = resumenHelper.construirResumen(percepciones, deducciones);

            totalPercepciones = totalPercepciones.add(resumen.percepciones());
            totalDeducciones = totalDeducciones.add(resumen.deducciones());

            if (tieneMovimiento(resumen)) {
                periodosConPago++;
                periodosConMovimiento.add(periodo);
            }
        }

        BigDecimal granTotalNeto = totalPercepciones.subtract(totalDeducciones);

        return new ResumenGlobalDto(
                periodosConPago,
                obtenerCatalogoPeriodos().size() - periodosConPago,
                periodosConMovimiento.isEmpty() ? null : periodosConMovimiento.get(0),
                periodosConMovimiento.isEmpty() ? null : periodosConMovimiento.get(periodosConMovimiento.size() - 1),
                totalPercepciones,
                totalDeducciones,
                granTotalNeto
        );
    }

    private List<String> obtenerCatalogoPeriodos() {
        return java.util.stream.IntStream.rangeClosed(1, 24)
                .mapToObj(i -> String.format("%02d", i))
                .toList();
    }

    private boolean tieneMovimiento(ResumenPeriodoDto resumen) {
        return resumen.percepciones().compareTo(BigDecimal.ZERO) > 0
                || resumen.deducciones().compareTo(BigDecimal.ZERO) > 0;
    }
}
