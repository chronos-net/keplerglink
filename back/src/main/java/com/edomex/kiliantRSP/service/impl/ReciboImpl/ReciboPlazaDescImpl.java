package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.service.ReciboService.ReciboPlazaDescService;
import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboPlazaDto;
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
public class ReciboPlazaDescImpl implements ReciboPlazaDescService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ReciboPlazaDescImpl.class);

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
    public ReciboPlazaDto obtenerPlaza(String neyemp, int anio, String quincena) {

        String tabla1 = "kddes" + anio;
        String tabla2 = "kdnom" + anio;
        String tabla3 = "kdnmctgpues" + anio;
        String tabla4 = "kdnmctgdep" + anio;
        String tabla5 = "kddes" + anio;
        String tabla6 = "kddesx" + anio;

        String puesto;

        try {

            if (anio < 2011) {

                String sqlPuesto = String.format(
                        "SELECT puesto FROM %s WHERE neyemp = ? AND periodo = ? LIMIT 1",
                        tabla5
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

            String sql = String.format("""
                SELECT
                    a.plaza AS plazaId,
                    a.adsc,
                    a.puesto,
                    c.leyenda_puesto AS leyendaPuesto,
                    d.dependencia AS dependencia,
                    a.lugar_pago AS centroTrabajo,
                    b.lug_pago AS lugarPago
                FROM %s a
                LEFT JOIN %s b ON a.neyemp = b.neyemp AND a.periodo = b.periodo
                LEFT JOIN %s c ON ? = c.puesto AND a.periodo = c.periodo
                LEFT JOIN %s d ON a.adsc = d.dependencia AND a.periodo = d.periodo
                WHERE a.neyemp = ? AND a.periodo = ?
                ORDER BY a.cve_empleado ASC
                LIMIT 1;
                """, tabla1, tabla2, tabla3, tabla4);

            return jdbcTemplate.queryForObject(
                    sql,
                    rowMapper,
                    puesto,
                    neyemp,
                    quincena
            );

        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No encontramos la plaza principal",
                    e
            );
        } catch (Exception e) {
            log.error("Error ejecutando query en recibo desc plazas", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al consultar la plaza",
                    e
            );
        }
    }
}
