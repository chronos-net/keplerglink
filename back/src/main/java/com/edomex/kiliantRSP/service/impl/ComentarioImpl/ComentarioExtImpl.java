package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;
import com.edomex.kiliantRSP.service.ComentarioService.ComentarioExtService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioExtImpl implements ComentarioExtService {

    private final JdbcTemplate jdbcTemplate;

    private record ExtRaw(
            Long id,
            String qna,
            String anio,
            String cuenta,
            String tipoMovimiento,
            BigDecimal neto,
            BigDecimal imp,
            String capturado,
            String comentario,
            String fechaCaptura,
            String adscripcion,
            String descripcion,
            String puesto,
            String leyendaPuesto
    ) {}

    private final RowMapper<ExtRaw> mapper = (rs, rowNum) -> new ExtRaw(
            rs.getLong("cve_empleado"),
            rs.getString("quincena"),
            rs.getString("anio"),
            rs.getString("cuenta"),
            rs.getString("tipo_movimiento"),
            rs.getBigDecimal("neto"),
            rs.getBigDecimal("imp"),
            rs.getString("capturado"),
            rs.getString("comentario"),
            rs.getString("fechaCaptura"),
            rs.getString("adscripcion"),
            rs.getString("descripcion"),
            rs.getString("puesto"),
            rs.getString("leyenda_puesto")
    );

    @Override
    public List<ComentarioValoresDto> obtenerExt(String neyemp){

        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (int anio = 2002; anio <= 2022; anio++) {

            String tableDep = "kdnmctgdep" + anio;
            String tablePuesto = "kdnmctgpues" + anio;

            // 🔥 obtener puesto base
            String puesto = obtenerPuesto(neyemp, anio);
            String puestoSql = (puesto == null) ? "NULL" : "'" + puesto + "'";

            // 🔥 JOIN dinámico
            String joinPuesto;
            if (anio < 2009) {
                joinPuesto = String.format("""
                    LEFT JOIN %s d
                        ON %s = d.puesto
                       AND a.quincena = d.periodo
                """, tablePuesto, puestoSql);
            } else {
                joinPuesto = String.format("""
                    LEFT JOIN %s d
                        ON a.puesto = d.puesto
                       AND a.quincena = d.periodo
                """, tablePuesto);
            }

            if (sqlBuilder.length() > 0) {
                sqlBuilder.append(" UNION ALL ");
            }

            sqlBuilder.append(String.format("""
                SELECT
                    a.cve_empleado,
                    a.quincena,
                    a.anio,
                    a.cuenta,
                    a.tipo_movimiento,
                    a.neto,
                    a.imp,
                    a.autorizado_por AS capturado,
                    CONCAT(a.motivo,' ', a.mot1, ' ', a.mot2) AS comentario,
                    a.fecha::varchar AS "fechaCaptura",
                    a.ads AS adscripcion,
                    b.descripcion,
                    a.puesto,
                    d.leyenda_puesto

                FROM kdaboext a

                LEFT JOIN %s b
                    ON a.ads = b.dependencia
                   AND a.quincena = b.periodo

                %s

                WHERE a.neyemp = ?
                  AND a.anio = '%d'
            """, tableDep, joinPuesto, anio));

            params.add(neyemp);
        }

        String finalSql = """
            SELECT * FROM (
                SELECT DISTINCT ON (cve_empleado) *
                FROM (
                    %s
                ) t
                ORDER BY cve_empleado, anio DESC, quincena DESC
            ) final
            ORDER BY anio DESC, quincena DESC
        """.formatted(sqlBuilder.toString());

        List<ExtRaw> rawList = jdbcTemplate.query(finalSql, mapper, params.toArray());

        return rawList.stream().map(r -> {

            // 🔹 formaPago
            String formaPago = (r.cuenta() != null && !r.cuenta().isBlank())
                    ? "ABONO"
                    : "OTRO";

            // 🔹 movimiento
            String tipo = r.tipoMovimiento() == null ? "" : r.tipoMovimiento();

            String movimiento = switch (tipo) {
                case "1" -> "BLOQUEO CUENTA";
                case "2" -> "MODIFICACION EXTEMPORANEA";
                case "3" -> "ALTA EXTEMPORANEA";
                case "4" -> "ABONO REACTIVACION";
                case "5" -> "RECHAZO BANCARIO";
                case "6" -> "SOLIC CARGO AL BANCO";
                default -> "OTRO";
            };

            String numCuenta = r.cuenta();

            // 🔹 importes
            BigDecimal neto = r.neto() != null ? r.neto() : BigDecimal.ZERO;
            BigDecimal imp  = r.imp()  != null ? r.imp()  : BigDecimal.ZERO;

            BigDecimal importeInicial;
            BigDecimal importeFinal;
            BigDecimal diferencia;

            switch (tipo) {

                case "2" -> {
                    importeInicial = neto.subtract(imp);
                    importeFinal   = imp;
                    diferencia     = neto;
                }

                case "3", "4" -> {
                    importeInicial = BigDecimal.ZERO;
                    importeFinal   = imp;
                    diferencia     = imp;
                }

                case "5", "6" -> {
                    importeInicial = imp;
                    importeFinal   = imp;
                    diferencia     = BigDecimal.ZERO;
                }

                default -> {
                    importeInicial = BigDecimal.ZERO;
                    importeFinal   = BigDecimal.ZERO;
                    diferencia     = BigDecimal.ZERO;
                }
            }

            // 🔹 DTOs
            AdscripcionDto adscripcion = (r.adscripcion() != null || r.descripcion() != null)
                    ? new AdscripcionDto(r.adscripcion(), r.descripcion())
                    : null;

            PuestoDto puestoDto = (r.puesto() != null || r.leyendaPuesto() != null)
                    ? new PuestoDto(r.puesto(), r.leyendaPuesto())
                    : null;

            LugarPagoDto lugarPago = null;

            return new ComentarioValoresDto(
                    "kdaboext",
                    r.id(),
                    r.qna(),
                    r.anio(),
                    movimiento,
                    formaPago,
                    numCuenta,
                    importeInicial,
                    importeFinal,
                    diferencia,
                    null,
                    r.capturado(),
                    r.comentario(),
                    r.fechaCaptura(),
                    adscripcion,
                    puestoDto,
                    lugarPago
            );

        }).toList();
    }

    // 🔥 versión sin periodo (igual que DEV global)
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