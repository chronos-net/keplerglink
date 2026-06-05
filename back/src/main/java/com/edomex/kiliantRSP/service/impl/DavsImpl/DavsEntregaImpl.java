package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsEntregadoDto;
import com.edomex.kiliantRSP.service.DavsService.DavsHentrega;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DavsEntregaImpl implements DavsHentrega{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DavsEntregadoDto> rowMapper = (rs, rowNum) -> new DavsEntregadoDto(
            rs.getInt("cveKdm1"),
            rs.getString("folioDocumento"),
            rs.getString("neyemp"),
            rs.getString("negnom"),
            rs.getDate("fecha")

    );

    @Override
    public List<DavsEntregadoDto> obtenerEntrega(String neyemp, String negnom){
        String tableName = "h_entregado";

        String sql = String.format("""
                SELECT 
                    a.cve_kdm1 AS cveKdm1,
                    a.folio_documento AS folioDocumento,
                    a.cliente_o_proveedor AS neyemp,
                    a.nombre_cliente AS negnom,
                    a.fecha
                FROM %s a
                WHERE a.cliente_o_proveedor = ?    
                """,tableName);

        try{
            List<DavsEntregadoDto> result = jdbcTemplate.query(sql, rowMapper, neyemp);

            if (result.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el registro de entrega"
                );
            }

            return result;

        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe el registro de entrega"
            );
        }
    }
}
