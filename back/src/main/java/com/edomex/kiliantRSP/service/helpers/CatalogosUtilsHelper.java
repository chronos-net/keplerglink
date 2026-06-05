package com.edomex.kiliantRSP.service.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CatalogosUtilsHelper {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Extrae claves catálogo dinámicas
     * prefijo:
     * per -> percepciones
     * ded -> deducciones
     */
    public Set<String> extraerClavesCatalogo(
            ResultSet rs,
            String prefijo
    ) throws SQLException {

        Set<String> claves = new HashSet<>();

        // NORMAL
        extraerRango(rs, claves, prefijo, "", 1, 10);

        // BLOQUE A
        extraerRango(rs, claves, prefijo, "A", 1, 20);

        // BLOQUE B
        extraerRango(rs, claves, prefijo, "B", 1, 20);

        return claves;
    }

    /**
     * Extrae columnas dinámicas por rango
     */
    private void extraerRango(
            ResultSet rs,
            Set<String> claves,
            String prefijo,
            String sufijo,
            int inicio,
            int fin
    ) {

        for (int i = inicio; i <= fin; i++) {

            String columna = prefijo + i + sufijo;

            String valor = obtenerSeguro(rs, columna);

            if (esValorValido(valor)) {
                claves.add(normalizarClave(valor));
            }
        }
    }

    /**
     * Obtiene valor seguro si columna no existe
     */
    public String obtenerSeguro(ResultSet rs, String columna) {
        try {
            return rs.getString(columna);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validación catálogo válida
     */
    public boolean esValorValido(String valor) {
        return valor != null
                && !valor.trim().isEmpty()
                && !valor.trim().equals("0");
    }

    /**
     * Normaliza clave catálogo
     */
    public String normalizarClave(String clave) {
        return clave.trim().toUpperCase();
    }

    /**
     * Construye Map final catálogo SIN descripción (1996–2001)
     */
    public Map<String, String> construirMapaCatalogo(
            Set<String> claves,
            Map<String, String> descripciones
    ) {

        Map<String, String> resultado = new TreeMap<>();

        for (String clave : claves) {

            String desc = descripciones != null
                    ? descripciones.getOrDefault(clave, "")
                    : "";

            resultado.put(clave, desc);
        }

        return resultado;
    }

    /**
     * 🔥 Construye Map catálogo consultando BD (2002–2022)
     */
    public Map<String,String> construirMapaCatalogoDesdeBD(
            Set<String> claves,
            Integer anio
    ){

        if(claves == null || claves.isEmpty()){
            return new TreeMap<>();
        }

        try {

            String tableName = "kdnmctgcon" + anio;

            String inSql = String.join(",", Collections.nCopies(claves.size(), "?"));

            String sql = String.format("""
            SELECT DISTINCT ON (clave) clave, descripcion
            FROM %s
            WHERE clave IN (%s)
        """, tableName, inSql);

            return jdbcTemplate.query(sql, claves.toArray(), rs -> {

                Map<String,String> mapa = new TreeMap<>();

                while(rs.next()){
                    mapa.put(
                            rs.getString("clave"),
                            rs.getString("descripcion")
                    );
                }

                return mapa;
            });

        } catch (Exception e){
            e.printStackTrace();
            return new TreeMap<>();
        }
    }

}
