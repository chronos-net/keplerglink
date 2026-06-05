package com.edomex.kiliantRSP.service.impl.ReciboImpl.ComentarioXRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboCanService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioReciboCan implements ComentarioReciboCanService {

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
    public List<ComentarioValoresDto> obtenerCan(String neyemp, String periodo, int anio){

        String tableNom = "kdnom" + anio;
        String tableDep = "kdnmctgdep" + anio;
        String tablePuesto = "kdnmctgpues" + anio;

        String puesto = obtenerPuesto(neyemp, periodo, anio);
        String puestoSql = (puesto == null) ? "NULL" : "'" + puesto + "'";

        String joinPuesto;
        if (anio < 2009) {
            joinPuesto = """
                LEFT JOIN %s p
                    ON %s = p.puesto
                   AND a.quincena = p.periodo
            """.formatted(tablePuesto, puestoSql);
        } else {
            joinPuesto = """
                LEFT JOIN %s p
                    ON a.categoria = p.puesto
                   AND a.quincena = p.periodo
            """.formatted(tablePuesto);
        }

        String sql = """
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
                CONCAT(a.motivo_1,' ', a.motivo_3, ' ', a.motivo_2) AS comentario,
                a.columna_13 AS "fechaCaptura",

                a.adscripcion,
                dep.descripcion,
                a.categoria AS puesto,
                p.leyenda_puesto,

                c.clave_lugar_2 AS "claveLugarPago",
                c.nombre_municipio AS "nombreMuni"

            FROM kdabocan a

            LEFT JOIN %s b
                ON a.neyemp = b.neyemp
               AND a.quincena = b.periodo

            LEFT JOIN %s dep
                ON a.adscripcion = dep.dependencia
               AND a.quincena = dep.periodo

            %s

            LEFT JOIN kdmun c
                ON LPAD(TRIM(a.columna_28), 7, '0') = LPAD(TRIM(c.clave_lugar_2), 7, '0')
                OR a.columna_28 = c.codigo_verdadero

            WHERE a.neyemp = ?
              AND a.quincena = ?
              AND a.anio = ?
        """.formatted(tableNom, tableDep, joinPuesto);

        List<CanRaw> rawList = jdbcTemplate.query(
                sql,
                mapper,
                neyemp,
                periodo,
                String.valueOf(anio)
        );

        return rawList.stream().map(r -> {

            String tipo = r.columna40() != null ? r.columna40() : "";

            // 🔹 cuenta
            String numCuenta = (r.columna27() == null || r.columna27().isBlank())
                    ? r.numCuenta()
                    : "00" + r.columna27();

            // 🔥 NUEVA REGLA PRIORITARIA
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

            // 🔹 importes
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

            LugarPagoDto lugarPago = null;

            if ((r.claveLugarPago() != null && !r.claveLugarPago().isBlank())
                    || (r.nombreMuni() != null && !r.nombreMuni().isBlank())) {

                lugarPago = new LugarPagoDto(
                        r.claveLugarPago(),
                        r.nombreMuni()
                );
            }

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
                    new AdscripcionDto(r.adscripcion(), r.descripcion()),
                    new PuestoDto(r.puesto(), r.leyendaPuesto()),
                    lugarPago
            );

        }).toList();
    }

    private String obtenerPuesto(String neyemp, String periodo, int anio) {
        String tabla5 = "kddes" + anio;
        String tabla6 = "kddesx" + anio;

        try {
            String sql;

            if (anio < 2011) {
                sql = String.format(
                        "SELECT puesto FROM %s WHERE neyemp = ? AND periodo = ? LIMIT 1",
                        tabla5
                );
            } else {
                sql = String.format(
                        "SELECT puesto FROM %s WHERE neyemp = ? AND periodo = ? LIMIT 1",
                        tabla6
                );
            }

            String puesto = jdbcTemplate.queryForObject(sql, String.class, neyemp, periodo);

            if (puesto != null && puesto.length() > 0 && anio < 2009) {
                puesto = puesto.substring(0, puesto.length() - 1);
            }

            return puesto;

        } catch (Exception e) {
            return null;
        }
    }
}