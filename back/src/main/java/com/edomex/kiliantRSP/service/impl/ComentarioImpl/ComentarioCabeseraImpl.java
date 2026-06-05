package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioCabesera;
import com.edomex.kiliantRSP.service.ComentarioService.ComentarioCabeseraService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ComentarioCabeseraImpl implements ComentarioCabeseraService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ComentarioCabesera> rowMapper = (rs, rowNum) -> new ComentarioCabesera(
            rs.getString("neyemp"),
            rs.getString("nombreCompleto"),
            rs.getString("rfc"),
            rs.getString("curp"),
            rs.getString("imss")
    );

    @Override
    public ComentarioCabesera obtenerCabesera(String neyemp){
        String tableName = "kdrhemp";

        String sql = String.format("""
                SELECT 
                    clave_del_empledo AS neyemp, nombre_completo AS nombreCompleto, rfc, curp, imss
                FROM %s 
                WHERE clave_del_empledo = ?
                """, tableName);

        try{
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp);
        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "cabesera no encontrada"
            );
        }
    }
}
