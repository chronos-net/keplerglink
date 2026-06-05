package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboEmleadoDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboEmpleadoService;
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
public class ReciboEmpleadoImpl implements ReciboEmpleadoService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReciboEmleadoDto> rowMapper = (rs, rowNum) -> new ReciboEmleadoDto(
            rs.getString("neyemp"),
            rs.getString("nombre"),
            rs.getString("curp"),
            rs.getString("rfc"),
            rs.getString("iss")
    );

    @Override
    public ReciboEmleadoDto obtenerReciboEmpelado(String neyemp, int anio, String quincena){

        String tableName = "kdrhemp";

        String sql = String.format("""
                SELECT
                     clave_del_empledo AS  neyemp,
                    nombre_completo AS nombre,
                    curp,
                     rfc, 
                     imss AS iss
                FROM %s 
                WHERE clave_del_empledo = ?  
                """, tableName);

        try {
            return  jdbcTemplate.queryForObject(sql, rowMapper, neyemp);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Datos del cp para el recibo no encontrados",
                    e
            );
        }
    }
}
