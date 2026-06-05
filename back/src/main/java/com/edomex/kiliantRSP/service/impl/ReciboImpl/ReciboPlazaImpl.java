package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboPlazaDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboPlazaService;
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
public class ReciboPlazaImpl implements ReciboPlazaService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReciboPlazaDto> rowMapper = (rs, rowNum) ->  new ReciboPlazaDto(
            rs.getString("plazaId"),
            rs.getString("adsc"),
            rs.getString("puesto"),
            rs.getString("leyendaPuesto"),
            rs.getString("dependencia"),
            rs.getString("centroTrabajo"),
            rs.getString("lugarPago")
    );

    @Override
    public ReciboPlazaDto obtenerPlaza(String neyemp, int anio, String quincena){
        String tablePrimaria = "kddes" + anio;
        String tableSegundo = "kdnom" + anio;

        String sql = String.format("""
                SELECT 
                    a.plaza AS plazaId, a.adsc,a.puesto,NULL AS leyendaPuesto,NULL AS dependencia,
                    a.lugar_pago AS centroTrabajo, b.lug_pago AS lugarPago
                FROM %s a
                LEFT JOIN %s b ON a.neyemp = b.neyemp AND a.periodo = b.periodo
                WHERE a.neyemp = ? AND a.periodo = ? LIMIT 1
                """,tablePrimaria, tableSegundo);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena);
        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro el plaza",
                    e
            );
        }
    }
}
