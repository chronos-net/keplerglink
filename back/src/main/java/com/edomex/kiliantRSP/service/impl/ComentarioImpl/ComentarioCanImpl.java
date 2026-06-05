package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;
import com.edomex.kiliantRSP.service.ComentarioService.ComentarioCanService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioCanImpl implements ComentarioCanService {

    private final JdbcTemplate jdbcTemplate;

    private record CanRaw(
            Long id,
            String qna,
            String anio,
            String columna40,
            String columna27,
            String columna23,
            BigDecimal col17,
            BigDecimal col18,
            BigDecimal col19,
            String numCuenta,
            String capturado,
            String comentario,
            String fechaCaptura,
            String adscripcion,
            String descripcion,
            String puesto,
            String leyendaPuesto,
            String claveLugarPago,
            String nombreMuni
    ) {}

    private final RowMapper<CanRaw> mapper = (rs, rowNum) -> new CanRaw(
            rs.getLong("cve_kdavocan"),
            rs.getString("quincena"),
            rs.getString("anio"),
            rs.getString("columna_40"),
            rs.getString("columna_27"),
            rs.getString("columna_23"),
            rs.getBigDecimal("columna_17"),
            rs.getBigDecimal("columna_18"),
            rs.getBigDecimal("columna_19"),
            rs.getString("numCuenta"),
            rs.getString("capturado"),
            rs.getString("comentario"),
            rs.getString("fechaCaptura"),
            rs.getString("adscripcion"),
            rs.getString("descripcion"),
            rs.getString("puesto"),
            rs.getString("leyenda_puesto"),
            rs.getString("claveLugarPago"),
            rs.getString("nombreMuni")
    );

    @Override
    public List<ComentarioValoresDto> obtenerCan(String neyemp){

        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (int anio = 2002; anio <= 2022; anio++) {

            String tableNom = "kdnom" + anio;
            String tableDep = "kdnmctgdep" + anio;
            String tablePuesto = "kdnmctgpues" + anio;

            String puesto = obtenerPuesto(neyemp, anio);
            String puestoSql = (puesto == null) ? "NULL" : "'" + puesto + "'";

            String joinPuesto;
            if (anio < 2009) {
                joinPuesto = String.format("""
                    LEFT JOIN %s p
                        ON %s = p.puesto
                       AND a.quincena = p.periodo
                """, tablePuesto, puestoSql);
            } else {
                joinPuesto = String.format("""
                    LEFT JOIN %s p
                        ON a.categoria = p.puesto
                       AND a.quincena = p.periodo
                """, tablePuesto);
            }

            if (sqlBuilder.length() > 0) {
                sqlBuilder.append(" UNION ALL ");
            }

            sqlBuilder.append(String.format("""
                SELECT
                    a.cve_kdavocan,
                    a.quincena,
                    a.anio,
                    a.columna_40,
                    a.columna_27,
                    a.columna_23,
                    a.columna_17,
                    a.columna_18,
                    a.columna_19,

                    b.num_cuenta AS "numCuenta",
                    a.autorizados_por AS capturado,
                    CONCAT(a.motivo_1,' ', a.motivo_2, ' ', a.motivo_3) AS comentario,
                    a.columna_13 AS "fechaCaptura",

                    a.adscripcion,
                    dep.descripcion,
                    a.categoria AS puesto,
                    p.leyenda_puesto,

                    COALESCE(c.clave_lugar_2, c2.clave_lugar_2) AS "claveLugarPago",
                    COALESCE(c.nombre_municipio, c2.nombre_municipio) AS "nombreMuni"

                FROM kdabocan a

                LEFT JOIN %s b
                    ON a.neyemp = b.neyemp
                   AND a.quincena = b.periodo

                LEFT JOIN %s dep
                    ON a.adscripcion = dep.dependencia
                   AND a.quincena = dep.periodo

                %s

                -- 🔥 JOIN limpio
                LEFT JOIN kdmun c
                    ON LPAD(TRIM(CAST(a.columna_28 AS VARCHAR)), 7, '0')
                     = LPAD(TRIM(CAST(c.clave_lugar_2 AS VARCHAR)), 7, '0')

                -- 🔥 fallback
                LEFT JOIN kdmun c2
                    ON a.columna_28 = c2.codigo_verdadero

                WHERE a.neyemp = ?
                  AND CAST(a.anio AS VARCHAR) = '%d'
            """, tableNom, tableDep, joinPuesto, anio));

            params.add(neyemp);
        }

        String finalSql = """
            SELECT * FROM (
                SELECT DISTINCT ON (cve_kdavocan) *
                FROM (
                    %s
                ) t
                ORDER BY cve_kdavocan, anio DESC, quincena DESC
            ) final
            ORDER BY anio DESC, quincena DESC
        """.formatted(sqlBuilder.toString());

        List<CanRaw> rawList = jdbcTemplate.query(finalSql, mapper, params.toArray());

        return rawList.stream().map(r -> {

            String tipo = r.columna40() != null ? r.columna40() : "";

            // 🔥 cuenta
            String numCuenta = (r.columna27() == null || r.columna27().isBlank())
                    ? r.numCuenta()
                    : "00" + r.columna27();

            // 🔥 REGLA PRIORITARIA
            boolean esChequePorCuenta =
                    numCuenta != null &&
                            numCuenta.length() == 7 &&
                            numCuenta.startsWith("00");

            String formaPago;

            if (esChequePorCuenta) {
                formaPago = "CHEQUE";
            } else {
                formaPago = switch (tipo) {
                    case "C", "R" -> "CHEQUE";
                    case "B", "V" -> "ABONO";
                    default -> "OTRO";
                };
            }

            BigDecimal importeInicial;
            BigDecimal importeFinal;
            BigDecimal diferencia;

            switch (tipo) {
                case "B", "V" -> {
                    importeInicial = r.col19() != null ? r.col19() : BigDecimal.ZERO;
                    importeFinal = BigDecimal.ZERO;
                    diferencia = importeInicial.negate();
                }
                case "C", "R" -> {
                    importeInicial = r.col17() != null ? r.col17() : BigDecimal.ZERO;
                    importeFinal = r.col18() != null ? r.col18() : BigDecimal.ZERO;
                    diferencia = r.col19() != null ? r.col19() : BigDecimal.ZERO;
                }
                default -> {
                    importeInicial = BigDecimal.ZERO;
                    importeFinal = BigDecimal.ZERO;
                    diferencia = BigDecimal.ZERO;
                }
            }

            String pensionado = ("C".equals(tipo) || "R".equals(tipo))
                    ? r.columna23()
                    : null;

            AdscripcionDto adscripcion = (r.adscripcion() != null || r.descripcion() != null)
                    ? new AdscripcionDto(r.adscripcion(), r.descripcion())
                    : null;

            PuestoDto puestoDto = (r.puesto() != null || r.leyendaPuesto() != null)
                    ? new PuestoDto(r.puesto(), r.leyendaPuesto())
                    : null;

            LugarPagoDto lugarPago = (r.claveLugarPago() != null || r.nombreMuni() != null)
                    ? new LugarPagoDto(r.claveLugarPago(), r.nombreMuni())
                    : null;

            return new ComentarioValoresDto(
                    "kdabocan",
                    r.id(),
                    r.qna(),
                    r.anio(),
                    "CANCELACION",
                    formaPago,
                    numCuenta,
                    importeInicial,
                    importeFinal,
                    diferencia,
                    pensionado,
                    r.capturado(),
                    r.comentario(),
                    r.fechaCaptura(),
                    adscripcion,
                    puestoDto,
                    lugarPago
            );

        }).toList();
    }

    private String obtenerPuesto(String neyemp, int anio) {

        String tabla = (anio < 2011) ? "kddes" + anio : "kddesx" + anio;

        try {
            String sql = String.format(
                    "SELECT puesto FROM %s WHERE neyemp = ? LIMIT 1",
                    tabla
            );

            String puesto = jdbcTemplate.queryForObject(sql, String.class, neyemp);

            if (puesto != null && puesto.length() > 0 && anio < 2009) {
                puesto = puesto.substring(0, puesto.length() - 1);
            }

            return puesto;

        } catch (Exception e) {
            return null;
        }
    }
}