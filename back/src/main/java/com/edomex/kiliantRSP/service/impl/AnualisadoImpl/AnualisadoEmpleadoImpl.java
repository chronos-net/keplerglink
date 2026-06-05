package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoEmpleadoDto;
import com.edomex.kiliantRSP.service.AnualisadoService.AnualisadoEmpleadoService;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.PlazasBase1996Impl;
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
public class AnualisadoEmpleadoImpl implements AnualisadoEmpleadoService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(PlazasBase1996Impl.class);

    private final RowMapper<AnualisadoEmpleadoDto> rowMapper = (rs, rowNum) -> new AnualisadoEmpleadoDto(
            rs.getString("id"),
            rs.getString("nombre"),
            rs.getString("curp"),
            rs.getString("rfc"),
            rs.getString("issemym")
    );

    @Override
    public AnualisadoEmpleadoDto obtenerEmpleado(String neyemp, int anio) {
        String tablaName = "kdrhemp";

        String sql = String.format("""
                SELECT
                    a.clave_del_empledo AS id, a.nombre_completo AS nombre, a.curp, a.rfc, 
                    a.imss AS issemym
                FROM %s a
                WHERE a.clave_del_empledo = ?; 
                """, tablaName);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp);
        }catch (Exception e) {
            log.error("Error ejecutando query PlazasBase1996", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error consultando plazas base",
                    e
            );
        }
    }
}
