package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;
import com.edomex.kiliantRSP.service.ComentarioService.ComentarioDevService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioDevImpl implements ComentarioDevService {

    private final JdbcTemplate jdbcTemplate;

    private record DevRaw(
            Long id,
            String qna,
            String anio,
            String cheque,
            String chequeCanc,
            BigDecimal neto,
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

    private final RowMapper<DevRaw> mapper = (rs, rowNum) -> new DevRaw(
            rs.getLong("cve_kdabodev"),
            rs.getString("quincena"),
            rs.getString("anio"),
            rs.getString("cheque"),
            rs.getString("cheque_canc"),
            rs.getBigDecimal("neto"),
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
    public List<ComentarioValoresDto> obtenerDev(String neyemp){

        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (int anio = 2002; anio <= 2022; anio++) {

            String tableNom = "kdnom" + anio;
            String tableDep = "kdnmctgdep" + anio;
            String tablePuesto = "kdnmctgpues" + anio;

            // 🔥 obtener puesto dinámico (sin periodo en este caso)
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
                    a.cve_kdabodev,
                    a.quincena,
                    a.anio,
                    a.cheque,
                    a.cheque_canc,
                    a.neto,
                    b.num_cuenta AS "numCuenta",
                    a.capturado,
                    CONCAT(a.motivo_1, '', a.motivo_2, '', a.motivo_3) AS comentario,
                    a.fecha_captura::varchar AS "fechaCaptura",
                    a.adscripcion,
                    dep.descripcion,
                    a.categoria AS puesto,
                    p.leyenda_puesto,
                    c.nombre_municipio AS "nombreMuni",
                    a.lugar_pago2 AS "claveLugarPago"

                FROM kdabodev a

                LEFT JOIN %s b
                    ON a.neyemp = b.neyemp
                   AND a.quincena = b.periodo

                LEFT JOIN %s dep
                    ON a.adscripcion = dep.dependencia
                   AND a.quincena = dep.periodo

                %s

                LEFT JOIN kdmun c
                    ON LPAD(a.lugar_pago2, 7, '0') = LPAD(c.clave_lugar_2, 7, '0') 

                WHERE a.neyemp = ?
                  AND a.anio = '%d'
            """, tableNom, tableDep, joinPuesto, anio));

            params.add(neyemp);
        }

        String finalSql = """
            SELECT * FROM (
                SELECT DISTINCT ON (cve_kdabodev) *
                FROM (
                    %s
                ) t
                ORDER BY cve_kdabodev, anio DESC, quincena DESC
            ) final
            ORDER BY anio DESC, quincena DESC
        """.formatted(sqlBuilder.toString());

        List<DevRaw> rawList = jdbcTemplate.query(finalSql, mapper, params.toArray());

        return rawList.stream().map(r -> {

            String cheque = r.cheque();

            // 🔥 PRIMERO: leer cheque_canc
            String tipo = r.chequeCanc() == null ? "" : r.chequeCanc().trim();

            // normalizar (por si viene '05', ' 5', etc)
            tipo = tipo.replaceAll("^0+", "");

            // 🔥 REGLA REAL
            boolean esAbono = "5".equals(tipo);

            // resultado
            String formaPago = esAbono ? "ABONO" : "CHEQUE";

            // 🔥 clave: aquí decides correctamente
            String numCuenta = esAbono ? r.numCuenta() : cheque;

            String movimiento = switch (tipo) {
                case "1" -> "RETENCION EXTEMPORANEA";
                case "2" -> "CANCELACION EXTEMPORANEA";
                case "3" -> "LIBERACION";
                case "4" -> "CHEQUE NO COBRADO";
                default -> "COMENTARIO";
            };

            BigDecimal importeInicial = r.neto() != null ? r.neto() : BigDecimal.ZERO;
            BigDecimal importeFinal = BigDecimal.ZERO;
            BigDecimal diferencia = importeInicial.negate();

            AdscripcionDto adscripcion = (r.adscripcion() != null || r.descripcion() != null)
                    ? new AdscripcionDto(r.adscripcion(), r.descripcion())
                    : null;

            PuestoDto puestoDto = (r.puesto() != null || r.leyendaPuesto() != null)
                    ? new PuestoDto(r.puesto(), r.leyendaPuesto())
                    : null;

            LugarPagoDto lugarPago = (r.claveLugarPago() != null && !r.claveLugarPago().isBlank())
                    ? new LugarPagoDto(r.claveLugarPago(), r.nombreMuni())
                    : null;

            return new ComentarioValoresDto(
                    "kdabodev",
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

    // 🔥 versión adaptada sin periodo
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