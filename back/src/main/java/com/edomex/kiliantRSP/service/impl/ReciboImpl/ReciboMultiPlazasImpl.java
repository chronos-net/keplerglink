package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.service.ReciboService.ReciboMultiPlazasService;
import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboMultiPlazasDto;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.PlazasBase1996Impl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReciboMultiPlazasImpl implements ReciboMultiPlazasService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ReciboMultiPlazasImpl.class);

    private RowMapper<ReciboMultiPlazasDto> rowMapper = (rs, rowNum) -> new ReciboMultiPlazasDto(
            rs.getString("plaza"),
            rs.getString("puesto"),
            rs.getString("leyendaPuesto"),
            rs.getString("adsc"),
            rs.getBigDecimal("neto")
    );

    @Override
    public List<ReciboMultiPlazasDto> obtenerMultiPlazas(String neyemp, int anio, String quincena){

        String tablePrimaria = "kddes" + anio;
        String tableSegundo = "vista_percepciones_" + anio;
        String tableTerceiro = "vista_deducciones_" + anio;
        String tabla6 = "kddesx" + anio;

        String puesto;

        System.out.println("estamos en el año:"+anio);
        if (anio < 2011) {
            System.out.println("ENTRO A <2011");
            String sqlPuesto = String.format(

                    "SELECT puesto FROM %s WHERE neyemp = ? AND periodo = ? LIMIT 1",
                    tablePrimaria
            );

            puesto = jdbcTemplate.queryForObject(
                    sqlPuesto,
                    String.class,
                    neyemp,
                    quincena
            );

            // 🔹 quitar último carácter (ej: T0111104 -> T011110)
            if (puesto != null && puesto.length() > 0) {
                puesto = puesto.substring(0, puesto.length() - 1);
            }

        } else {
            System.out.println("ENTRO A >2011");
            String sqlPuesto = String.format(
                    "SELECT puesto FROM %s WHERE neyemp = ? AND periodo = ? LIMIT 1",
                    tabla6
            );

            puesto = jdbcTemplate.queryForObject(
                    sqlPuesto,
                    String.class,
                    neyemp,
                    quincena
            );
        }

        System.out.println(puesto);

        String sql = String.format("""
                SELECT
                    base.plaza,
                    base.puesto,
                    NULL AS leyendaPuesto,
                    base.adsc,
                    tot.neto
                FROM (
                    SELECT DISTINCT ON (a.secuencia_plaza)
                           a.*
                    FROM %s a
                    WHERE a.neyemp = ?
                      AND a.periodo = ?
                    ORDER BY a.secuencia_plaza, a.cve_empleado DESC
                ) base
                LEFT JOIN (
                    SELECT
                        p.secuencia_plaza,
                        p.total AS total_percepciones,
                        COALESCE(d.total, 0) AS total_deducciones,
                        (p.total - COALESCE(d.total, 0)) AS neto
                    FROM (
                        SELECT DISTINCT ON (secuencia_plaza)
                               secuencia_plaza, total
                        FROM %s
                        WHERE neyemp = ?
                          AND periodo = ?
                        ORDER BY secuencia_plaza, cve_empleado ASC
                    ) p
                    LEFT JOIN (
                        SELECT DISTINCT ON (secuencia_plaza)
                               secuencia_plaza, total
                        FROM %s
                        WHERE neyemp = ?
                          AND periodo = ?
                        ORDER BY secuencia_plaza, cve_neyemp ASC
                    ) d
                    ON p.secuencia_plaza = d.secuencia_plaza
                ) tot
                ON base.secuencia_plaza = tot.secuencia_plaza;
                """,tablePrimaria, tableSegundo, tableTerceiro);

        try{
            return jdbcTemplate.query(sql, rowMapper, neyemp, quincena, neyemp, quincena, neyemp, quincena);
        }catch (Exception e){
            log.error("Error ejecutando query PlazasBase1996", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "no se encontro un desglose de plazas",
                    e
            );
        }
    }
}
