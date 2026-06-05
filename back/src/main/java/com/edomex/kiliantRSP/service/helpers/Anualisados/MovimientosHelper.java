package com.edomex.kiliantRSP.service.helpers.Anualisados;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.MovimientoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MovimientosHelper {

    private final JdbcTemplate jdbcTemplate;
    private final KddesAnualizadoResolver kddesResolver;

    public List<MovimientoDto> obtenerPercepciones(
            String neyemp,
            int anio,
            String periodo
    ) {

        List<MovimientoDto> lista = obtenerMovimientos(
                "vista_percepciones_" + anio,
                "per",
                neyemp,
                periodo,
                anio
        );

        return compactarMovimientos(lista);
    }

    public List<MovimientoDto> obtenerDeducciones(
            String neyemp,
            int anio,
            String periodo
    ) {

        List<MovimientoDto> lista = obtenerMovimientos(
                "vista_deducciones_" + anio,
                "ded",
                neyemp,
                periodo,
                anio
        );

        return compactarMovimientos(lista);
    }

    private List<MovimientoDto> obtenerMovimientos(
            String tabla,
            String prefijo,
            String neyemp,
            String periodo,
            int anio
    ) {

        List<String> secuenciasValidas =
                kddesResolver.obtenerSecuenciasValidas(neyemp, anio, periodo);

        if (secuenciasValidas.isEmpty()) {
            return new ArrayList<>();
        }

        String inSql = String.join(
                ",",
                Collections.nCopies(secuenciasValidas.size(), "?")
        );

        String sql = """
            SELECT *
            FROM %s
            WHERE neyemp = ?
            AND periodo = ?
            AND periodo NOT IN ('A1', 'A2')
            AND secuencia_plaza::text IN (%s)
            """.formatted(tabla, inSql);

        List<Object> params = new ArrayList<>();
        params.add(neyemp);
        params.add(periodo);
        params.addAll(secuenciasValidas);

        return jdbcTemplate.query(sql, rs -> {

            List<MovimientoDto> lista = new ArrayList<>();

            while (rs.next()) {

                String[] sufijos = {"", "a", "b"};
                int[] limites = {10, 20, 20};

                for (int s = 0; s < sufijos.length; s++) {

                    for (int i = 1; i <= limites[s]; i++) {

                        String suf = sufijos[s];

                        String colImporte = "imp" + i + suf;
                        String colCodigo = prefijo + i + suf;

                        try {

                            BigDecimal importe =
                                    rs.getBigDecimal(colImporte);

                            String codigo =
                                    rs.getString(colCodigo);

                            if (importe != null
                                    && importe.compareTo(BigDecimal.ZERO) > 0
                                    && codigo != null
                                    && !codigo.isBlank()) {

                                lista.add(
                                        new MovimientoDto(
                                                codigo.trim(),
                                                importe
                                        )
                                );
                            }

                        } catch (Exception ignored) {
                            // columna no presente en esta vista/año
                        }
                    }
                }
            }

            return lista;

        }, params.toArray());
    }

    /**
     * Compacta movimientos repetidos sumando importes
     */
    private List<MovimientoDto> compactarMovimientos(
            List<MovimientoDto> lista
    ) {

        Map<String, BigDecimal> acumulado = new HashMap<>();

        for (MovimientoDto mov : lista) {

            acumulado.merge(
                    mov.codigo(),
                    mov.importe(),
                    BigDecimal::add
            );
        }

        List<MovimientoDto> resultado = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry
                : acumulado.entrySet()) {

            resultado.add(
                    new MovimientoDto(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        resultado.sort(
                Comparator.comparing(MovimientoDto::codigo)
        );

        return resultado;
    }
}
