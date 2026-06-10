package com.edomex.kiliantRSP.service.helpers.Anualisados;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeriodosKddesHelper {

    private final KddesAnualizadoResolver kddesResolver;

    public KddesRegistro obtenerUltimoRegistro(
            String neyemp,
            int anio,
            String periodo
    ) {
        var ultimoH = kddesResolver.obtenerUltimoHValido(neyemp, anio, periodo);

        if (ultimoH == null) {
            return null;
        }

        return new KddesRegistro(
                ultimoH.periodo(),
                ultimoH.secuenciaPlaza()
        );
    }

    public record KddesRegistro(
            String periodo,
            Integer secuenciaPlaza
    ) {}
}
