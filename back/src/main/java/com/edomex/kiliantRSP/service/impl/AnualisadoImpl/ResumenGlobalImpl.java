package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.ResumenGlobalDto;
import com.edomex.kiliantRSP.service.AnualisadoService.ResumenGlobalService;
import com.edomex.kiliantRSP.service.helpers.Anualisados.KddesAnualizadoResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumenGlobalImpl implements ResumenGlobalService {

    private final JdbcTemplate jdbcTemplate;
    private final KddesAnualizadoResolver kddesResolver;

    private final RowMapper<ResumenGlobalDto> rowMapper = (rs, rowNum) -> new ResumenGlobalDto(
            rs.getInt("periodosConPago"),
            rs.getInt("periodosSinPlaza"),
            rs.getString("primerPeriodo"),
            rs.getString("ultimoPeriodo"),
            rs.getBigDecimal("totalPercepciones"),
            rs.getBigDecimal("totalDeducciones"),
            rs.getBigDecimal("granTotalNeto")
    );

    @Override
    public ResumenGlobalDto obtenerResumenGlobal(String neyemp, int anio) {
        var fuente = kddesResolver.resolver(neyemp, anio);
        String tablaPlazas = fuente.tabla();
        String columnaSecuencia = fuente.columnaSecuencia();
        String tablePrimaria = "vista_percepciones_" + anio;
        String tableSegundo = "vista_deducciones_" + anio;

        String sql = String.format("""
                  WITH catalogo_periodos AS (
                    SELECT LPAD(gs::text, 2, '0') AS periodo
                    FROM generate_series(1,24) gs
                ),

                plazas_validas AS (
                    SELECT DISTINCT
                        periodo::text AS periodo,
                        %s::text AS secuencia
                    FROM %s
                    WHERE neyemp = ?
                      AND TRIM(tipo_nomina::text) IN ('0', '1')
                ),

                percepciones AS (
                    SELECT
                        vp.periodo,
                        SUM(vp.total) AS total_percepciones
                    FROM %s vp
                    JOIN plazas_validas pv
                      ON pv.periodo = vp.periodo::text
                     AND pv.secuencia = vp.secuencia_plaza::text
                    WHERE vp.neyemp = ?
                    GROUP BY vp.periodo
                ),

                deducciones AS (
                    SELECT
                        vd.periodo,
                        SUM(vd.total) AS total_deducciones
                    FROM %s vd
                    JOIN plazas_validas pv
                      ON pv.periodo = vd.periodo::text
                     AND pv.secuencia = vd.secuencia_plaza::text
                    WHERE vd.neyemp = ?
                    GROUP BY vd.periodo
                ),

                periodos_reales AS (
                    SELECT periodo FROM percepciones
                    UNION
                    SELECT periodo FROM deducciones
                ),

                periodos_faltantes AS (
                    SELECT c.periodo
                    FROM catalogo_periodos c
                    LEFT JOIN periodos_reales r
                        ON c.periodo = r.periodo
                    WHERE r.periodo IS NULL
                )

                SELECT
                    COUNT(pr.periodo) AS periodosConPago,
                    (SELECT COUNT(*) FROM periodos_faltantes) AS periodosSinPlaza,

                    MIN(pr.periodo) AS primerPeriodo,
                    MAX(pr.periodo) AS ultimoPeriodo,

                    COALESCE(SUM(p.total_percepciones),0) AS totalPercepciones,
                    COALESCE(SUM(d.total_deducciones),0) AS totalDeducciones,

                    COALESCE(SUM(p.total_percepciones),0)
                    - COALESCE(SUM(d.total_deducciones),0) AS granTotalNeto,

                    (SELECT STRING_AGG(periodo, ', ')
                     FROM periodos_faltantes) AS listaPeriodosFaltantes

                FROM periodos_reales pr
                LEFT JOIN percepciones p ON pr.periodo = p.periodo
                LEFT JOIN deducciones d ON pr.periodo = d.periodo;
                """, columnaSecuencia, tablaPlazas, tablePrimaria, tableSegundo);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, neyemp, neyemp);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "datos no envontrados", e
            );
        }
    }

}
