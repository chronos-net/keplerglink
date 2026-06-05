package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;
import com.edomex.kiliantRSP.service.ComentarioService.ComentariosCompService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentariosCompImpl implements ComentariosCompService {

    private final JdbcTemplate jdbcTemplate;

    // 🔹 Raw (todo viene como varchar desde BD)
    private record CompRaw(
            Long id,
            String qna,
            String anio,
            String movimiento,
            String formaPago,
            String cuenta,
            String importeInicial,
            String importeFinal,
            String diferencia,
            String capturado,
            String comentario,
            String fechaCaptura,
            String adscripcion,
            String descripcionAdscripcion,
            String puesto,
            String descripcionPuesto,
            String claveLugarPago,
            String descripcionLugarPago
    ) {}

    // 🔹 Mapper alineado con el SELECT
    private final RowMapper<CompRaw> mapper = (rs, rowNum) -> new CompRaw(
            rs.getLong("cve_comp"),
            rs.getString("quincena"),
            rs.getString("anio"),
            rs.getString("movimiento"),
            rs.getString("forma_pago"),
            rs.getString("cuenta"),
            rs.getString("imp"),
            rs.getString("imp_f"),
            rs.getString("diferencia"),
            rs.getString("realisado"),
            rs.getString("comentario"),
            rs.getString("fecha_captura"),
            rs.getString("adscripcon"),
            rs.getString("desc_adcripcion"),
            rs.getString("puesto"),
            rs.getString("desc_puesto"),
            rs.getString("lugar_pago"),
            rs.getString("desc_pago")
    );

    @Override
    public List<ComentarioValoresDto> obtenerComp(String neyemp) {

        String sql = """
            SELECT a.cve_comp, a.quincena, a.anio, a.movimiento, a.forma_pago, a.cuenta,
                   a.imp, a.imp_f, a.diferencia, a.realisado,
                   a.comentario, a.fecha_captura, a.adscripcon, a.desc_adcripcion,
                   a.puesto, a.desc_puesto, a.lugar_pago, a.desc_pago
            FROM kdaboext_comp a
            WHERE a.neyemp = ?
            """;

        List<CompRaw> rawList = jdbcTemplate.query(sql, mapper, neyemp);

        return rawList.stream().map(r -> {

            // 🔹 Conversión segura a BigDecimal
            BigDecimal impInicial = toBigDecimal(r.importeInicial());
            BigDecimal impFinal   = toBigDecimal(r.importeFinal());
            BigDecimal diff       = toBigDecimal(r.diferencia());

            // 🔹 Sub-objetos
            AdscripcionDto adscripcion = (r.adscripcion() != null || r.descripcionAdscripcion() != null)
                    ? new AdscripcionDto(r.adscripcion(), r.descripcionAdscripcion())
                    : null;

            PuestoDto puesto = (r.puesto() != null || r.descripcionPuesto() != null)
                    ? new PuestoDto(r.puesto(), r.descripcionPuesto())
                    : null;

            LugarPagoDto lugarPago = (r.claveLugarPago() != null && !r.claveLugarPago().isBlank())
                    ? new LugarPagoDto(r.claveLugarPago(), r.descripcionLugarPago())
                    : null;

            return new ComentarioValoresDto(
                    "kdaboext_comp",
                    r.id(),
                    r.qna(),
                    r.anio(),
                    safeStr(r.movimiento()),
                    safeStr(r.formaPago()),
                    safeStr(r.cuenta()),
                    impInicial,
                    impFinal,
                    diff,
                    null,
                    safeStr(r.capturado()),
                    safeStr(r.comentario()),
                    safeStr(r.fechaCaptura()),
                    adscripcion,
                    puesto,
                    lugarPago
            );

        }).toList();
    }

    // 🔹 Helpers

    private BigDecimal toBigDecimal(String value) {
        try {
            if (value == null) return BigDecimal.ZERO;

            String limpio = value
                    .replace(",", "")
                    .replace("$", "")
                    .trim();

            return limpio.isEmpty() ? BigDecimal.ZERO : new BigDecimal(limpio);

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private String safeStr(String s) {
        return (s == null) ? "" : s;
    }
}