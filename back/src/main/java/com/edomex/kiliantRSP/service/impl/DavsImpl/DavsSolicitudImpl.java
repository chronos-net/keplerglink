package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsSolicitudDto;
import com.edomex.kiliantRSP.service.DavsService.DavsHsolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DavsSolicitudImpl  implements DavsHsolicitud{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DavsSolicitudDto> rowMapper = (rs, rowMapper) -> new DavsSolicitudDto(
            rs.getInt("cveKdm1"),
            rs.getString("folioDocumento"),
            rs.getString("neyemp"),
            rs.getString("negnom"),
            rs.getDate("fecha"),
            rs.getString("tipoMovimiento")
    );

    @Override
    public List<DavsSolicitudDto> obtenerSolicitud(String neyemp, String negnom){
        String tableName = "h_solicitud";

        String sql = String.format("""
                SELECT
                    a.cve_kdm1 AS cveKdm1,
                     a.folio_documento AS folioDocumento,
                     a.cliente_o_proveedor AS neyemp,
                     a.nombre_cliente AS negnom,
                     a.fecha,
                     a.tipo_de_movimiento AS tipoMovimiento
                FROM  %s a
                WHERE a.cliente_o_proveedor = ? 
                """, tableName);

        try {
            List<DavsSolicitudDto> result = jdbcTemplate.query(sql, rowMapper, neyemp);

            if (result.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el registro de solicitud"
                );
            }

            return result;

        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro el registro para el solicitud"
            );
        }
    }
}
