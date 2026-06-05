package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsTramiteDto;
import com.edomex.kiliantRSP.service.DavsService.DavsHtramite;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DavsHtramiteImpl implements DavsHtramite{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DavsTramiteDto> rowMapper = (rs, rowNum) -> new  DavsTramiteDto(
            rs.getInt("cveKdm1"),
            rs.getString("folioDocumento"),
            rs.getString("neyemp"),
            rs.getString("negnom"),
            rs.getDate("fecha"),
            rs.getString("destinatarioCheque")
    );

    @Override
    public List<DavsTramiteDto> obtenerTramite(String neyemp, String negnom) {
        String tableName = "h_tramite";

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

        List<DavsTramiteDto> resultado = jdbcTemplate.query(sql, rowMapper, neyemp);

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron trámites"
            );
        }

        return resultado;
    }

}
