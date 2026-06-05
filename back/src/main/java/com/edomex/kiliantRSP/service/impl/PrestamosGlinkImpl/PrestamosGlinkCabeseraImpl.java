package com.edomex.kiliantRSP.service.impl.PrestamosGlinkImpl;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkCabesera;
import com.edomex.kiliantRSP.service.PrestamosGlinkService.PrestamosGlinkCabeseraService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrestamosGlinkCabeseraImpl implements PrestamosGlinkCabeseraService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PrestamosGlinkCabesera> rowMapper = (rs, rowNum) ->
            new PrestamosGlinkCabesera(
                    rs.getString("clavesp"),
                    rs.getString("nombre_sp"),
                    rs.getString("rfc"),
                    rs.getString("fecha_in")
            );

    @Override
    public PrestamosGlinkCabesera obtenerCabesera(String neyemp){

        System.out.println("obtenerCabesera: "+neyemp);
        String sql = """
            SELECT clavesp, nombre_sp, rfc, fecha_in
            FROM prestamos_glink
            WHERE clavesp = ?
            LIMIT 1
        """;

        var resultado = jdbcTemplate.query(sql, rowMapper, neyemp);

        System.out.println("Resultado JDBC: " + resultado);

        return resultado.isEmpty() ? null : resultado.get(0);
    }
}
