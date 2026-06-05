package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.PlazaBaseDto1996;
import com.edomex.kiliantRSP.service.DescPlazasService.PlazasBase2002Service;
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
public class PlazasBase2002Impl implements PlazasBase2002Service {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(PlazasBase1996Impl.class);

    private final RowMapper<PlazaBaseDto1996> rowMapper = (rs, rowNum) -> new PlazaBaseDto1996(
            rs.getString("plazaId"),
            rs.getString("secuenciaPlaza"),
            rs.getString("puesto"),
            rs.getString("leyendaPuesto"),
            rs.getString("lugpago"),
            rs.getString("centroTrabajo")
    );

    @Override
    public List<PlazaBaseDto1996> obtenerPlazasBase(String neyemp, Integer anio, String quincena){
        String tablePrimaria = "kddes" + anio;
        String tableSecundaria = "kdnmctgpues" + anio;

        String sql = String.format("""
                SELECT a.plaza AS plazaId, a.secuencia_plaza AS secuenciaPlaza, a.puesto, b.leyenda_puesto AS leyendaPuesto,
                a.lugar_pago AS lugpago, a.centro_trabajo AS centroTrabajo
                FROM %s a
                LEFT JOIN %s b
                  ON b.periodo = a.periodo
                 AND (
                      b.puesto = a.puesto
                      OR b.puesto = LEFT(a.puesto, 7)
                 )
                WHERE a.neyemp = ?
                  AND a.periodo = ?;
                """, tablePrimaria, tableSecundaria);


        try{
            return jdbcTemplate.query(sql, rowMapper, neyemp, quincena);
        } catch (Exception e){
            log.error("Error ejecutando query PlazasBase1996", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error consultando plazas base",
                    e
            );
        }
    }
}
