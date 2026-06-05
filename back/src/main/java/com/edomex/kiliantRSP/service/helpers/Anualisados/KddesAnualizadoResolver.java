package com.edomex.kiliantRSP.service.helpers.Anualisados;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KddesAnualizadoResolver {

    private final JdbcTemplate jdbcTemplate;

    public record KddesFuente(String tabla, String columnaSecuencia) {}

    public KddesFuente resolver(String neyemp, int anio) {
        if (anio > 2010) {
            return new KddesFuente("kddesx" + anio, "sec_plaza");
        }

        String tablaKddes = "kddes" + anio;
        if (contarRegistros(tablaKddes, neyemp) > 0) {
            return new KddesFuente(tablaKddes, "secuencia_plaza");
        }

        String tablaKddesx = "kddesx" + anio;
        if (tablaExiste(tablaKddesx) && contarRegistros(tablaKddesx, neyemp) > 0) {
            return new KddesFuente(tablaKddesx, "sec_plaza");
        }

        return new KddesFuente(tablaKddes, "secuencia_plaza");
    }

    /**
     * Secuencias de plaza válidas para anualizado: solo tipo_nomina ordinaria (0 o 1).
     * kddes usa secuencia_plaza; kddesx usa sec_plaza (equivalente en vistas).
     */
    public List<String> obtenerSecuenciasValidas(String neyemp, int anio, String periodo) {
        var fuente = resolver(neyemp, anio);

        String sql = """
                SELECT DISTINCT %s::text AS secuencia
                FROM %s
                WHERE neyemp = ?
                  AND periodo = ?
                  AND TRIM(tipo_nomina::text) IN ('0', '1')
                """.formatted(fuente.columnaSecuencia(), fuente.tabla());

        return jdbcTemplate.queryForList(sql, String.class, neyemp, periodo);
    }

    /**
     * CTE reutilizable: plazas con tipo_nomina ordinaria (0 o 1) por periodo y secuencia.
     * Requiere un parámetro neyemp en la consulta que la incorpore.
     */
    public String ctePlazasValidas(String neyemp, int anio) {
        var fuente = resolver(neyemp, anio);

        return """
                plazas_validas AS (
                    SELECT DISTINCT
                        periodo::text AS periodo,
                        %s::text AS secuencia
                    FROM %s
                    WHERE neyemp = ?
                      AND TRIM(tipo_nomina::text) IN ('0', '1')
                )
                """.formatted(fuente.columnaSecuencia(), fuente.tabla());
    }

    private int contarRegistros(String tabla, String neyemp) {
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE neyemp = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, neyemp);
        return count != null ? count : 0;
    }

    private boolean tablaExiste(String tabla) {
        Boolean exists = jdbcTemplate.queryForObject(
                "SELECT to_regclass(?) IS NOT NULL",
                Boolean.class,
                "public." + tabla
        );
        return Boolean.TRUE.equals(exists);
    }
}