package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.TotalGlobales1996Dto;
import com.edomex.kiliantRSP.service.DescPlazasService.PlazaTotalesGlobalesservice1996;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlazaTotalesGlobalesImpl1996 implements PlazaTotalesGlobalesservice1996 {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TotalGlobales1996Dto> rowMapper = (rs, rowMapper) -> new TotalGlobales1996Dto(
            rs.getBigDecimal("percepciones"),
            rs.getBigDecimal("deducciones"),
            rs.getBigDecimal("neto")
    );

    @Override
    public TotalGlobales1996Dto obtenerTotalGlobal(String neyemp, int anio, String quincena) {
        String tablePrincipal = "kdnom" + anio;

        String sql = String.format("""
                SELECT 
                    SUM(a.percep) AS percepciones,SUM(a.ded) AS deducciones,SUM(a.percep - a.ded) AS neto
                FROM %s a   
                WHERE a.neyemp = ? AND periodo = ?; 
                """, tablePrincipal);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron globales",
                    e
            );
        }
    }
}
