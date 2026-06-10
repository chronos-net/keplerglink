package com.edomex.kiliantRSP.service.helpers.Anualisados;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KddesAnualizadoResolver {

    private final JdbcTemplate jdbcTemplate;

    public record KddesFuente(String tabla, String columnaSecuencia) {}

    public record UltimoHValido(String periodo, Integer secuenciaPlaza) {}

    /**
     * @deprecated Usar {@link #obtenerTablaKddes(int)} para cabecera/cálculo según reglas OTSO.
     */
    @Deprecated
    public KddesFuente resolver(String neyemp, int anio) {
        if (anio > 2010) {
            return new KddesFuente("kddesx" + anio, "sec_plaza");
        }

        String tablaKddes = obtenerTablaKddes(anio);
        if (contarRegistros(tablaKddes, neyemp) > 0) {
            return new KddesFuente(tablaKddes, "secuencia_plaza");
        }

        String tablaKddesx = "kddesx" + anio;
        if (tablaExiste(tablaKddesx) && contarRegistros(tablaKddesx, neyemp) > 0) {
            return new KddesFuente(tablaKddesx, "sec_plaza");
        }

        return new KddesFuente(tablaKddes, "secuencia_plaza");
    }

    public String obtenerTablaKddes(int anio) {
        return "kddes" + anio;
    }

    /**
     * Último registro válido de KDDES (equivalente OTSO: VIEW(H) con H3 &lt; 30).
     */
    public UltimoHValido obtenerUltimoHValido(String neyemp, int anio, String periodo) {
        if (!tablaKddesDisponible(anio)) {
            return null;
        }

        String tabla = obtenerTablaKddes(anio);
        String sql = """
                SELECT periodo,
                       CAST(secuencia_plaza AS INTEGER) AS secuencia_plaza
                FROM %s
                WHERE neyemp = ?
                  AND periodo = ?
                  AND CAST(secuencia_plaza AS INTEGER) < 30
                  AND TRIM(tipo_nomina::text) IN ('0', '1')
                ORDER BY CAST(secuencia_plaza AS INTEGER) DESC,
                         cve_empleado DESC NULLS LAST
                LIMIT 1
                """.formatted(tabla);

        List<UltimoHValido> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new UltimoHValido(
                        rs.getString("periodo"),
                        rs.getInt("secuencia_plaza")
                ),
                neyemp,
                periodo
        );

        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Secuencias de plaza válidas para cálculo: siempre desde KDDES, tipo_nomina ordinaria (0 o 1).
     */
    public List<String> obtenerSecuenciasValidas(String neyemp, int anio, String periodo) {
        if (!tablaKddesDisponible(anio)) {
            return Collections.emptyList();
        }

        String tabla = obtenerTablaKddes(anio);
        String sql = """
                SELECT secuencia_plaza::text AS secuencia
                FROM %s
                WHERE neyemp = ?
                  AND periodo = ?
                  AND CAST(secuencia_plaza AS INTEGER) < 30
                  AND TRIM(tipo_nomina::text) IN ('0', '1')
                GROUP BY secuencia_plaza
                ORDER BY CAST(secuencia_plaza AS INTEGER)
                """.formatted(tabla);

        return jdbcTemplate.queryForList(sql, String.class, neyemp, periodo);
    }

    /**
     * CTE reutilizable: plazas válidas desde KDDES por periodo y secuencia.
     * Requiere un parámetro neyemp en la consulta que la incorpore.
     */
    public String ctePlazasValidas(String neyemp, int anio) {
        String tabla = obtenerTablaKddes(anio);

        return """
                plazas_validas AS (
                    SELECT DISTINCT
                        periodo::text AS periodo,
                        secuencia_plaza::text AS secuencia
                    FROM %s
                    WHERE neyemp = ?
                      AND CAST(secuencia_plaza AS INTEGER) < 30
                      AND TRIM(tipo_nomina::text) IN ('0', '1')
                )
                """.formatted(tabla);
    }

    private boolean tablaKddesDisponible(int anio) {
        return tablaExiste(obtenerTablaKddes(anio));
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
