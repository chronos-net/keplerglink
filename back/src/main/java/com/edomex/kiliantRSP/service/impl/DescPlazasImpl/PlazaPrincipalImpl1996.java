package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.PlazaPrincipal1996Dto;
import com.edomex.kiliantRSP.service.DescPlazasService.PlazaPrincipalService1996;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlazaPrincipalImpl1996 implements PlazaPrincipalService1996{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PlazaPrincipal1996Dto> rowMapper = (rs, rowNum) -> new PlazaPrincipal1996Dto(
            rs.getString("plazaId"),
            rs.getString("secuenciaPlaza")
    );

    @Override
    public PlazaPrincipal1996Dto obtenerPlazaPrincipal(String neyemp, int anio, String quincena) {
        try {
            if (anio <= 2001) {
                return obtenerPlazaPrincipalDesdeKddesx(neyemp, anio, quincena);
            }
            return obtenerPlazaPrincipalPorSecuenciaUno(neyemp, anio, quincena);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro el plaza"
            );
        }
    }

    private PlazaPrincipal1996Dto obtenerPlazaPrincipalDesdeKddesx(
            String neyemp,
            int anio,
            String quincena
    ) {
        String tableKddesx = "kddesx" + anio;
        String tableKddes = "kddes" + anio;

        String sql = String.format("""
                SELECT
                    d.plaza AS plazaId,
                    d.secuencia_plaza AS secuenciaPlaza
                FROM %s x
                INNER JOIN %s d
                    ON d.neyemp = x.neyemp
                   AND d.periodo = x.periodo
                   AND TRIM(d.plaza) = TRIM(x.plaza)
                   AND TRIM(d.adsc) = TRIM(x.ads)
                WHERE x.neyemp = ?
                  AND x.periodo = ?
                ORDER BY CAST(d.secuencia_plaza AS INTEGER) ASC,
                         d.cve_empleado ASC
                LIMIT 1
                """, tableKddesx, tableKddes);

        return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena);
    }

    private PlazaPrincipal1996Dto obtenerPlazaPrincipalPorSecuenciaUno(
            String neyemp,
            int anio,
            String quincena
    ) {
        String tableKddes = "kddes" + anio;

        String sql = String.format("""
                SELECT
                    a.plaza AS plazaId,
                    a.secuencia_plaza AS secuenciaPlaza
                FROM %s a
                WHERE a.neyemp = ? AND a.periodo = ? AND a.secuencia_plaza = '1'
                """, tableKddes);

        return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena);
    }
}
