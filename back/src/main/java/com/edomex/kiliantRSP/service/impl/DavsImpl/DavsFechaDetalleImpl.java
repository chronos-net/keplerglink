package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsFechaDto;
import com.edomex.kiliantRSP.service.DavsService.DavsFechaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.sql.Date;

@Service
@RequiredArgsConstructor
public class DavsFechaDetalleImpl implements DavsFechaDetalleService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(DavsDescTramiteImpl.class);

    private final RowMapper<DavsFechaDto> rowMapper = (rs, rowNum) -> new DavsFechaDto(
            rs.getString("concepto"),
            rs.getString("diaInicial"),
            rs.getString("mesInicial"),
            rs.getString("anioInicial"),
            rs.getString("diaFinal"),
            rs.getInt("mesFinal"),
            rs.getInt("anioFinal"),
            rs.getInt("aniosTotales"),
            rs.getInt("mesesTotales")
    );

    @Override
    public DavsFechaDto obtenerFechasDetalle(Long cveKdm1){

        String sqlPrevia = String.format("""
                SELECT 
                folio_documento AS folio 
                FROM h_tramite
                WHERE cve_kdm1 = ?
                """);

        String resultado = jdbcTemplate.queryForObject(sqlPrevia, String.class, cveKdm1);

        String folio = resultado;

        String sql = String.format("""
                                    SELECT
                    descripcion_del AS concepto,
                    cantidad_de_uni AS diaInicial,
                    unidad AS mesInicial,
                    precio_unitario AS anioInicial,
                    importe_de_la_p AS diaFinal,
                    descuento_porce_1 AS mesFinal,
                    descuento_porce_2 AS anioFinal,
                    descuento_porce_3 AS aniosTotales,
                    ieps_porcentual AS mesesTotales
                    FROM h_fecha
                    WHERE folio_documento = ?
        """);

        try {

            return  jdbcTemplate.queryForObject(sql, rowMapper, folio);

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
