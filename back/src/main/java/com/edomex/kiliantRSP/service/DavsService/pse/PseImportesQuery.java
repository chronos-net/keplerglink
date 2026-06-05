package com.edomex.kiliantRSP.service.DavsService.pse;

import com.edomex.kiliantRSP.dto.DavsDto.PseImporteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PseImportesQuery {

    private final JdbcTemplate jdbcTemplate;

    public List<PseImporteDto> obtenerImportes(
            Long cveKdm1,
            String clienteProveedor
    ) {

        String sql = """
            SELECT num_tipo_documento,
                   folio_documento_a_anexar,
                   destinatario_cheque,
                   tipo_de_movimiento,
                   importe,
                   importe AS importe2
            FROM kdm1
            WHERE folio_documento = (
                SELECT folio_documento_a_anexar
                FROM h_solicitud
                WHERE cve_kdm1 = ?
            )
            AND cliente_o_proveedor = ?

            UNION ALL

            SELECT num_tipo_documento,
                   folio_documento_a_anexar,
                   destinatario_cheque,
                   tipo_de_movimiento,
                   importe,
                   importe AS importe2
            FROM kdm1
            WHERE folio_documento = (
                SELECT destinatario_cheque
                FROM h_solicitud
                WHERE cve_kdm1 = ?
            )
            AND cliente_o_proveedor = ?

            UNION ALL

            SELECT num_tipo_documento,
                   folio_documento_a_anexar,
                   destinatario_cheque,
                   tipo_de_movimiento,
                   importe,
                   importe AS importe2
            FROM h_solicitud
            WHERE cve_kdm1 = ?
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new PseImporteDto(
                        rs.getString("num_tipo_documento"),
                        rs.getString("folio_documento_a_anexar"),
                        rs.getString("destinatario_cheque"),
                        rs.getString("tipo_de_movimiento"),
                        rs.getBigDecimal("importe"),
                        rs.getBigDecimal("importe2")
                ),
                cveKdm1,
                clienteProveedor,
                cveKdm1,
                clienteProveedor,
                cveKdm1
        );
    }
}
