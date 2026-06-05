package com.edomex.kiliantRSP.service.helpers.Anualisados;

import com.edomex.kiliantRSP.service.helpers.CatalogosUtilsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CatalogosDeduccionesDescHelper {

    private final JdbcTemplate jdbcTemplate;
    private final CatalogosUtilsHelper catalogosUtilsHelper;
    private final KddesAnualizadoResolver kddesResolver;

    public Map<String, String> obtenerDed(String neyemp, int anio){

        String tableName = "vista_deducciones_" + anio;
        String ctePlazasValidas = kddesResolver.ctePlazasValidas(neyemp, anio);

        String sql = String.format("""
                WITH %s
                SELECT vd.*
                FROM %s vd
                JOIN plazas_validas pv
                  ON pv.periodo = vd.periodo::text
                 AND pv.secuencia = vd.secuencia_plaza::text
                WHERE vd.neyemp = ?
                  AND vd.periodo NOT IN ('A1', 'A2')
                """, ctePlazasValidas, tableName);

        try {

            Set<String> claves = new HashSet<>();

            jdbcTemplate.query(sql, rs -> {
                claves.addAll(
                        catalogosUtilsHelper.extraerClavesCatalogo(rs, "ded")
                );
            }, neyemp, neyemp);

            if(claves.isEmpty()){
                return new TreeMap<>();
            }

            return catalogosUtilsHelper.construirMapaCatalogoDesdeBD(
                    claves,
                    anio
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new TreeMap<>();
        }
    }
}
