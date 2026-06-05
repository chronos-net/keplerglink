package com.edomex.kiliantRSP.service.impl.ReciboImpl.ComentarioXRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboExtService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioReciboExtImpl implements ComentarioReciboExtService {

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
    public List<ComentarioValoresDto> obtenerExt(String neyemp, String periodo, int anio){

        String tableDep = "kdnmctgdep" + anio;
        String tablePuesto = "kdnmctgpues" + anio;

        // 🔥 obtener puesto base
        String puesto = obtenerPuesto(neyemp, periodo, anio);
        String puestoSql = (puesto == null) ? "NULL" : "'" + puesto + "'";

        // 🔥 JOIN dinámico de puesto (igual que DEV/CAN)
        String joinPuesto;
        if (anio < 2009) {
            joinPuesto = """
                LEFT JOIN %s d
                    ON %s = d.puesto
                   AND a.quincena = d.periodo
            """.formatted(tablePuesto, puestoSql);
        } else {
            joinPuesto = """
                LEFT JOIN %s d
                    ON a.puesto = d.puesto
                   AND a.quincena = d.periodo
            """.formatted(tablePuesto);
        }

        String sql = """
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
              AND a.quincena = ?
              AND a.anio = ?
        """.formatted(tableDep, joinPuesto);

        List<ExtRaw> rawList = jdbcTemplate.query(
                sql,
                mapper,
                neyemp,
                periodo,
                String.valueOf(anio)
        );

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

            // 🔹 numCuenta
            String numCuenta = r.cuenta();

            // 🔹 importes
            BigDecimal neto = r.neto() != null ? r.neto() : BigDecimal.ZERO;
            BigDecimal imp  = r.imp()  != null ? r.imp()  : BigDecimal.ZERO;

            BigDecimal importeInicial;
            BigDecimal importeFinal;
            BigDecimal diferencia;

            switch (tipo) {

                case "2" -> { // MODIFICACION
                    importeInicial = neto.subtract(imp);
                    importeFinal   = imp;
                    diferencia     = neto;
                }

                case "3", "4" -> { // ALTA / REACTIVACION
                    importeInicial = BigDecimal.ZERO;
                    importeFinal   = imp;
                    diferencia     = imp;
                }

                case "5", "6" -> { // RECHAZO / CARGO
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

            // 🔹 EXT no maneja municipio real
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

            // 🔥 recorte correcto (<2009)
            if (puesto != null && puesto.length() > 0 && anio < 2009) {
                puesto = puesto.substring(0, puesto.length() - 1);
            }

            return puesto;

        } catch (Exception e) {
            return null;
        }
    }
}