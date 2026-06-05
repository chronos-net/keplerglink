package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboMultiPlazasDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboMultiplazasDescService;
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
public class ReciboMultiplazasDescImpl implements ReciboMultiplazasDescService {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<ReciboMultiPlazasDto> rowMapper = (rs, rowNum) -> new ReciboMultiPlazasDto(
            rs.getString("plaza"),
            rs.getString("puesto"),
            rs.getString("leyendaPuesto"),
            rs.getString("adsc"),
            rs.getBigDecimal("neto")
    );

    @Override
    public List<ReciboMultiPlazasDto> obtenerMultiplazas(String neyemp, int anio, String quincena){

        String table1 = "kddes" + anio;
        String table2 = "kdnmctgpues" + anio;
        String table3 = "vista_percepciones_" + anio;
        String table4 = "vista_deducciones_" + anio;
        String tabla6 = "kddesx" + anio;

        String puesto;

        System.out.println("estamos en el año:"+anio);

        if (anio < 2011) {
            System.out.println("ENTRO A <2011");

            String sqlPuesto = String.format(
                    "SELECT puesto FROM %s WHERE neyemp = ? AND periodo = ? LIMIT 1",
                    table1
            );

            puesto = jdbcTemplate.queryForObject(
                    sqlPuesto,
                    String.class,
                    neyemp,
                    quincena
            );

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

        System.out.println("PUESTO FINAL: " + puesto);

        String sql = String.format("""
            SELECT
                base.plaza,
                base.puesto,
                c.leyenda_puesto AS leyendaPuesto,
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
            
            -- 🔥 AQUÍ ESTÁ LA CLAVE
            LEFT JOIN %s c
                   ON ? = c.puesto
                  AND base.periodo = c.periodo
            
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
            """, table1, table2, table3, table4);

        try{
            return jdbcTemplate.query(
                    sql,
                    rowMapper,
                    neyemp, quincena,
                    puesto,              // 🔥 AQUÍ PASAS EL PUESTO
                    neyemp, quincena,
                    neyemp, quincena
            );
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro un desglose de plazas",
                    e
            );
        }
    }
}
