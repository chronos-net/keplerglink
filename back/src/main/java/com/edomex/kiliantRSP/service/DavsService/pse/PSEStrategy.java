package com.edomex.kiliantRSP.service.DavsService.pse;

import com.edomex.kiliantRSP.dto.DavsDto.DavsSolicitudApoyoDto;
import com.edomex.kiliantRSP.dto.DavsDto.PseDetalleDto;
import com.edomex.kiliantRSP.service.DavsService.strategy.DestinatarioChequeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PSEStrategy implements DestinatarioChequeStrategy {

    private final JdbcTemplate jdbcTemplate;
    private final PseImportesQuery pseImportesQuery;

    @Override
    public Object obtenerDetalle(DavsSolicitudApoyoDto solicitud){
        /// variables
        Calendar cal = Calendar.getInstance();
        cal.setTime(solicitud.fecha());

        int anioe = cal.get(Calendar.YEAR);

        String tipo = solicitud.tipoMovimiento().substring(0,1);

        int sindicato = solicitud.plazoDias();
        String sindicatoStr = String.valueOf(sindicato);



        var importes = pseImportesQuery.obtenerImportes(
                solicitud.cveKdm1(),
                solicitud.clienteProveedor()
        );


        String sqlMensaje = """
                SELECT CONCAT(
                    comentario1,' ',
                    comentario2,' ',
                    comentario3,' ',
                    comentario4
                ) AS comentario
                FROM kdaacattexto
                WHERE anio = ?
                AND prestacion = ?
                AND sindicato = ?
            """;

        String mensaje = jdbcTemplate.queryForObject(
                sqlMensaje,
                String.class,
                String.valueOf(anioe),
                tipo,
                sindicatoStr
        );

        return new PseDetalleDto(importes, mensaje);


    }

    @Override
    public String getTipo() {
        return "PSE";
    }
}
