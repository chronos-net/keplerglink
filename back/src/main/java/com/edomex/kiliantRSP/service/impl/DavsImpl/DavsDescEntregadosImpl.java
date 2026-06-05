package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescEntregadoDto;
import com.edomex.kiliantRSP.dto.DavsDto.DavsDescEntregadoRDto;
import com.edomex.kiliantRSP.dto.DavsDto.DavsDescTramiteRDto;
import com.edomex.kiliantRSP.service.DavsService.DavsDescEntregadosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DavsDescEntregadosImpl implements DavsDescEntregadosService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DavsDescEntregadoRDto> rowMapper = (rs, rowNum) -> new DavsDescEntregadoRDto(
            rs.getString("unidadAdministrativa"),
            rs.getInt("tipo"),
            rs.getString("importeII"),
            rs.getString("movimiento"),
            rs.getString("folio"),
            rs.getString("fecha"),
            rs.getString("horaInicio"),
            rs.getString("horaFinal"),
            rs.getString("claveServidor"),
            rs.getString("enFavorDe"),
            rs.getString("rfc"),
            rs.getBigDecimal("importe"),
            rs.getString("mensajeCompleto"),
            rs.getLong("cveKdm1")
    );

    @Override
    public DavsDescEntregadoDto obtenerDescEntregado(Long cveKdm1) {

        String sql = """
            SELECT
                sucursal AS unidadAdministrativa,
                a.num_tipo_documento AS tipo,
                a.saldo_documento AS importeII,
                a.tipo_de_movimiento AS movimiento,
                a.folio_documento AS folio,
                a.fecha AS fecha,
                a.hora_de_captura AS horaInicio,
                NULL::text AS horaFinal,
                a.cliente_o_proveedor AS claveServidor,
                a.comentario_1 AS enFavorDe,
                a.comentario_2 AS rfc,
                a.importe AS importe,
        
                COALESCE(a.comentarios1, '') || ' ' ||
                COALESCE(a.comentarios2, '') || ' ' ||
                COALESCE(a.comentarios3, '') || ' ' ||
                COALESCE(a.comentarios4, '') || ' ' ||
                COALESCE(a.comentarios5, '') || ' ' ||
                COALESCE(a.comentarios6, '') || ' ' ||
                COALESCE(a.comentarios7, '') || ' ' ||
                COALESCE(a.comentarios8, '') AS mensajeCompleto,
        
                a.cve_kdm1 as cveKdm1
            FROM kdm1 a
            WHERE a.cve_kdm1 = ?
        """;

        try {

            DavsDescEntregadoRDto result =
                    jdbcTemplate.queryForObject(sql, rowMapper, cveKdm1);

            return new DavsDescEntregadoDto(
                    result.unidadAdministrativa(),
                    result.tipo(),
                    result.importeII(),
                    result.movimiento(),
                    result.folio(),
                    result.fecha(),
                    result.horaInicio(),
                    result.horaFinal(),
                    result.claveServidor(),
                    result.enFavorDe(),
                    result.rfc(),
                    result.importe(),
                    result.mensajeCompleto(),
                    result.cveKdm1()
            );

        } catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro la entrega"
            );
        }
    }
}
