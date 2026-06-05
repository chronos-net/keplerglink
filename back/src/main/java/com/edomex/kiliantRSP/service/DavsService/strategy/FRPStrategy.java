package com.edomex.kiliantRSP.service.DavsService.strategy;

import com.edomex.kiliantRSP.dto.DavsDto.DavsSolicitudApoyoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FRPStrategy implements DestinatarioChequeStrategy {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Object obtenerDetalle(DavsSolicitudApoyoDto solicitud) {

        int anio = obtenerAnio(solicitud.fechaPago());

        String sql = """
            SELECT CONCAT(
                comentario1,' ',
                comentario2,' ',
                comentario3,' ',
                comentario4
            )
            FROM kdaacattexto
            WHERE anio = ?
              AND prestacion = LEFT(?,1)
              AND sindicato = '3'
        """;

        return jdbcTemplate.queryForObject(
                sql,
                String.class,
                anio,
                solicitud.tipoMovimiento()
        );
    }

    private int obtenerAnio(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.YEAR);
    }

    @Override
    public String getTipo() {
        return "FRP";
    }
}
