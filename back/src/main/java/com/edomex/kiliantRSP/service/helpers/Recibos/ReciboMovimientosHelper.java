package com.edomex.kiliantRSP.service.helpers.Recibos;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboConceptoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReciboMovimientosHelper {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Obtiene movimientos (percepciones o deducciones) de un recibo
     * @param tabla nombre de la vista (ej. vista_percepciones_1996)
     * @param neyemp clave del empleado
     * @param periodo quincena/periodo
     * @param tipo "per" para percepciones, "ded" para deducciones
     * @return lista de ReciboConceptoDto
     */
    public List<ReciboConceptoDto> obtenerMovimientos(
            String tabla,
            String neyemp,
            String periodo,
            String tipo
    ) {

        String sql = """
            SELECT *
            FROM %s
            WHERE neyemp = ?
            AND periodo = ?
            AND periodo NOT IN ('A1', 'A2');
        """.formatted(tabla);

        return jdbcTemplate.query(sql, rs -> {

            List<ReciboConceptoDto> lista = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {

                // ===== IMP1 - IMP10 =====
                for (int i = 1; i <= 10; i++) {
                    String colCodigo = tipo + i;
                    String colImporte = "imp" + i;

                    if (columnaExiste(meta, colCodigo) && columnaExiste(meta, colImporte)) {
                        String codigo = rs.getString(colCodigo);
                        BigDecimal importe = rs.getBigDecimal(colImporte);
                        if (importe != null && importe.compareTo(BigDecimal.ZERO) > 0) {
                            lista.add(new ReciboConceptoDto(codigo, "", importe));
                        }
                    }
                }

                // ===== IMP1A - IMP20A =====
                for (int i = 1; i <= 20; i++) {
                    String colCodigo = tipo + i + "a";
                    String colImporte = "imp" + i + "a";

                    if (columnaExiste(meta, colCodigo) && columnaExiste(meta, colImporte)) {
                        String codigo = rs.getString(colCodigo);
                        BigDecimal importe = rs.getBigDecimal(colImporte);
                        if (importe != null && importe.compareTo(BigDecimal.ZERO) > 0) {
                            lista.add(new ReciboConceptoDto(codigo, "", importe));
                        }
                    }
                }

                // ===== IMP1B - IMP20B =====
                for (int i = 1; i <= 20; i++) {
                    String colCodigo = tipo + i + "b";
                    String colImporte = "imp" + i + "b";

                    if (columnaExiste(meta, colCodigo) && columnaExiste(meta, colImporte)) {
                        String codigo = rs.getString(colCodigo);
                        BigDecimal importe = rs.getBigDecimal(colImporte);
                        if (importe != null && importe.compareTo(BigDecimal.ZERO) > 0) {
                            lista.add(new ReciboConceptoDto(codigo, "", importe));
                        }
                    }
                }

            }

            return lista;

        }, neyemp, periodo);
    }

    /** Verifica si la columna existe en el ResultSet */
    private boolean columnaExiste(ResultSetMetaData meta, String nombreColumna) {
        try {
            int columns = meta.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                if (meta.getColumnName(i).equalsIgnoreCase(nombreColumna)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // ignoramos
        }
        return false;
    }
}