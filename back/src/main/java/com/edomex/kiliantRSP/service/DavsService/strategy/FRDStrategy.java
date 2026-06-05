package com.edomex.kiliantRSP.service.DavsService.strategy;

import com.edomex.kiliantRSP.dto.DavsDto.DavsSolicitudApoyoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FRDStrategy implements DestinatarioChequeStrategy {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Object obtenerDetalle(DavsSolicitudApoyoDto solicitudBase) {

        int anio = obtenerAnio(solicitudBase.fecha());

        String sql = """
            SELECT CONCAT(
                comentario1,' ',
                comentario2,' ',
                comentario3,' ',
                comentario4
            ) AS comentario
            FROM kdaacattexto
            WHERE anio = CAST(? AS VARCHAR)
              AND prestacion = 'F'
              AND sindicato = CAST(? AS VARCHAR)
            """;


        return jdbcTemplate.queryForObject(
                sql,
                String.class,
                anio,                          // int → CAST a varchar
                solicitudBase.plazoDias()      // int → CAST a varchar
        );

    }

    private int obtenerAnio(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.YEAR);
    }


    @Override
    public String getTipo() {
        return "FRD";
    }
}
