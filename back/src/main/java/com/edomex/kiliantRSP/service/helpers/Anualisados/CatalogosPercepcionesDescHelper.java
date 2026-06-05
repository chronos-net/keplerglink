package com.edomex.kiliantRSP.service.helpers.Anualisados;

import com.edomex.kiliantRSP.service.helpers.CatalogosUtilsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CatalogosPercepcionesDescHelper {

    private final JdbcTemplate jdbcTemplate;
    private final CatalogosUtilsHelper catalogosUtilsHelper;
    private final KddesAnualizadoResolver kddesResolver;

    public Map<String, String> obtenerPer(String neyemp, int anio){

        String tableName = "vista_percepciones_" + anio;
        String ctePlazasValidas = kddesResolver.ctePlazasValidas(neyemp, anio);

        String sql = String.format("""
                WITH %s
                SELECT vp.*
                FROM %s vp
                JOIN plazas_validas pv
                  ON pv.periodo = vp.periodo::text
                 AND pv.secuencia = vp.secuencia_plaza::text
                WHERE vp.neyemp = ?
                  AND vp.periodo NOT IN ('A1', 'A2')
                """, ctePlazasValidas, tableName);

        try {

            Set<String> claves = new HashSet<>();

            jdbcTemplate.query(sql, rs -> {
                claves.addAll(
                        catalogosUtilsHelper.extraerClavesCatalogo(rs, "per")
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
