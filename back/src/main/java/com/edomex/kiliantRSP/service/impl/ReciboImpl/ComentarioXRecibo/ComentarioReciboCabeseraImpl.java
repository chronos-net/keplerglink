package com.edomex.kiliantRSP.service.impl.ReciboImpl.ComentarioXRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioCabesera;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboCabeseraService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComentarioReciboCabeseraImpl implements ComentarioReciboCabeseraService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ComentarioCabesera> rowMapper = (rs, rowNum) -> new ComentarioCabesera(
            rs.getString("neyemp"),
            rs.getString("nombreCompleto"),
            rs.getString("rfc"),
            rs.getString("curp"),
            rs.getString("imss")
    );

    @Override
    public ComentarioCabesera obtenerCabeseraService(String neyemp, String periodo, int anio){

        String sql = String.format("""
                SELECT 
                    clave_del_empledo AS neyemp, nombre_completo AS nombreCompleto, rfc, curp, imss
                FROM kdrhemp
                WHERE clave_del_empledo = ?
                """);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Datos del empleado no encontrado",
                    e
            );
        }
    }
}