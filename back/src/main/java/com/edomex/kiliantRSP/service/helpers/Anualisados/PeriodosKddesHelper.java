package com.edomex.kiliantRSP.service.helpers.Anualisados;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PeriodosKddesHelper {

    private final JdbcTemplate jdbcTemplate;
    private final KddesAnualizadoResolver kddesResolver;

    public KddesRegistro obtenerUltimoRegistro(
            String neyemp,
            int anio,
            String periodo
    ) {

        var fuente = kddesResolver.resolver(neyemp, anio);
        String tabla = fuente.tabla();
        String columnaSecuencia = fuente.columnaSecuencia();

        String sql = """
                SELECT periodo,
                       %s AS secuencia_plaza
                FROM %s
                WHERE neyemp = ?
                  AND periodo = ?
                  AND TRIM(tipo_nomina::text) IN ('0', '1')
                ORDER BY %s DESC
                LIMIT 1
                """.formatted(columnaSecuencia, tabla, columnaSecuencia);

        List<KddesRegistro> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new KddesRegistro(
                        rs.getString("periodo"),
                        rs.getInt("secuencia_plaza")
                ),
                neyemp,
                periodo
        );

        return result.isEmpty() ? null : result.get(0);
    }

    public record KddesRegistro(
            String periodo,
            Integer secuenciaPlaza
    ) {}
}