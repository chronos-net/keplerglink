package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsAsesoriaDto;
import com.edomex.kiliantRSP.service.DavsService.DavsHasesoria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DavsHasesoriaImpl implements DavsHasesoria {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DavsAsesoriaDto> rowMapper = (rs, rowNum) -> new DavsAsesoriaDto(
            rs.getInt("cveKdm1"),
            rs.getString("folioDocumento"),
            rs.getString("neyemp"),
            rs.getString("negnom"),
            rs.getDate("fecha"),
            rs.getString("destinatarioCheque")
    );

    @Override
    public List<DavsAsesoriaDto> obtenerAsesoria(String neyemp, String negnom) {
        String tableName = "h_asesoria";

        String sql = String.format("""
                SELECT
                    a.cve_kdm1 AS cveKdm1,
                    a.folio_documento AS folioDocumento,
                    a.cliente_o_proveedor AS neyemp,
                    a.nombre_cliente AS negnom,
                    a.fecha,
                    a.destinatario_cheque AS destinatarioCheque
                FROM %s a
                WHERE a.cliente_o_proveedor = ?    
                """, tableName);

        try {
            List<DavsAsesoriaDto> result = jdbcTemplate.query(sql, rowMapper, neyemp);

            if (result.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el registro de asesoría"
                );
            }

            return result;

        } catch (Exception e) {
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro el registro de asesoria"
            );
        }
    }
}
