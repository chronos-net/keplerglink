package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ResciboResumenDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboTotalService;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class ReciboTotalImpl implements ReciboTotalService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ResciboResumenDto> rowMapper = (rs, rowNum) -> new ResciboResumenDto(
            rs.getBigDecimal("totalPercepciones"),
            rs.getBigDecimal("totalDeducciones"),
            rs.getBigDecimal("neto")
    );

    @Override
    public ResciboResumenDto obtenerTotal(String neyemp, int anio, String quincena){

        String tableName = "kdnom" + anio;

        String sql = String.format("""
                SELECT 
                    a.percep AS totalPercepciones, a.ded AS totalDeducciones, 
                    SUM (a.percep - a.ded) AS neto
                FROM %s a
                WHERE a.neyemp = ? AND a.periodo = ?
                GROUP BY
                    a.percep,
                    a.ded
                """,tableName);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena);
        }catch (Exception e){
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "no se encontraron totales",
                    e
            );
        }
    }
}
