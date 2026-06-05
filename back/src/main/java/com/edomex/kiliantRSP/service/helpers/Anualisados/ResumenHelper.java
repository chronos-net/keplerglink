package com.edomex.kiliantRSP.service.helpers.Anualisados;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.MovimientoDto;
import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.ResumenPeriodoDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ResumenHelper {

    public ResumenPeriodoDto construirResumen(
            List<MovimientoDto> percepciones,
            List<MovimientoDto> deducciones
    ) {

        BigDecimal totalPer = percepciones.stream()
                .map(MovimientoDto::importe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDed = deducciones.stream()
                .map(MovimientoDto::importe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumenPeriodoDto(
                totalPer,
                totalDed,
                totalPer.subtract(totalDed)
        );
    }
}