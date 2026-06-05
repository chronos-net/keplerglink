package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.PercepcionesPlazaDto;
import com.edomex.kiliantRSP.service.DescPlazasService.Percepciones1996Service;
import com.edomex.kiliantRSP.Mapper.DescPlazasMapper.DatosPercepcionesPlazasMapper;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.DescPlazasDatosPercepciones;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.DescPlazasDatosPercepcionesSimp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Percepciones1996impl implements Percepciones1996Service {

    private final JdbcTemplate              jdbcTemplate;
    private final DatosPercepcionesPlazasMapper mapper;

    private final RowMapper<DescPlazasDatosPercepciones> rowMapper = (rs, rowNum) -> new DescPlazasDatosPercepciones(
            rs.getString("secuencia_plaza"),
            rs.getBigDecimal("total"),
            rs.getString("per1"), rs.getString("per2"), rs.getString("per3"), rs.getString("per4"), rs.getString("per5"),
            rs.getString("per6"), rs.getString("Per7"), rs.getString("per8"), rs.getString("Per9"), rs.getString("per10"),
            rs.getBigDecimal("imp1"), rs.getBigDecimal("imp2"), rs.getBigDecimal("imp3"), rs.getBigDecimal("imp4"), rs.getBigDecimal("imp5"),
            rs.getBigDecimal("imp6"), rs.getBigDecimal("Imp7"), rs.getBigDecimal("Imp8"), rs.getBigDecimal("Imp9"), rs.getBigDecimal("Imp10"),

            rs.getString("per1A"), rs.getString("per2A"), rs.getString("per3A"), rs.getString("per4A"), rs.getString("per5A"),
            rs.getString("per6A"), rs.getString("Per7A"), rs.getString("per8A"), rs.getString("Per9A"), rs.getString("per10A"),
            rs.getString("per11A"), rs.getString("per12A"), rs.getString("per13A"), rs.getString("per14A"), rs.getString("per15A"),
            rs.getString("per16A"), rs.getString("Per17A"), rs.getString("per18A"), rs.getString("Per19A"), rs.getString("per20A"),

            rs.getBigDecimal("imp1A"), rs.getBigDecimal("imp2A"), rs.getBigDecimal("imp3A"), rs.getBigDecimal("imp4A"), rs.getBigDecimal("imp5A"),
            rs.getBigDecimal("imp6A"), rs.getBigDecimal("Imp7A"), rs.getBigDecimal("Imp8A"), rs.getBigDecimal("Imp9A"), rs.getBigDecimal("Imp10A"),
            rs.getBigDecimal("imp11A"), rs.getBigDecimal("imp12A"), rs.getBigDecimal("imp13A"), rs.getBigDecimal("imp14A"), rs.getBigDecimal("imp15A"),
            rs.getBigDecimal("imp16A"), rs.getBigDecimal("Imp17A"), rs.getBigDecimal("Imp18A"), rs.getBigDecimal("Imp19A"), rs.getBigDecimal("Imp20A"),

            rs.getString("per1B"), rs.getString("per2B"), rs.getString("per3B"), rs.getString("per4B"), rs.getString("per5B"),
            rs.getString("per6B"), rs.getString("Per7B"), rs.getString("per8B"), rs.getString("Per9B"), rs.getString("per10B"),
            rs.getString("per11B"), rs.getString("per12B"), rs.getString("per13B"), rs.getString("per14B"), rs.getString("per15B"),
            rs.getString("per16B"), rs.getString("Per17B"), rs.getString("per18B"), rs.getString("Per19B"), rs.getString("per20B"),

            rs.getBigDecimal("imp1B"), rs.getBigDecimal("imp2B"), rs.getBigDecimal("imp3B"), rs.getBigDecimal("imp4B"), rs.getBigDecimal("imp5B"),
            rs.getBigDecimal("imp6B"), rs.getBigDecimal("Imp7B"), rs.getBigDecimal("Imp8B"), rs.getBigDecimal("Imp9B"), rs.getBigDecimal("Imp10B"),
            rs.getBigDecimal("imp11B"), rs.getBigDecimal("imp12B"), rs.getBigDecimal("imp13B"), rs.getBigDecimal("imp14B"), rs.getBigDecimal("imp15B"),
            rs.getBigDecimal("imp16B"), rs.getBigDecimal("Imp17B"), rs.getBigDecimal("Imp18B"), rs.getBigDecimal("Imp19B"), rs.getBigDecimal("Imp20B")

    );

    @Override
    public List<PercepcionesPlazaDto> obtenerPercepciones(
            String neyemp,
            int anio,
            String quincena
    ) {

        String tableName = "vista_percepciones_" + anio;

        String sql = String.format("""
            SELECT *
            FROM %s
            WHERE neyemp = ? AND periodo = LPAD(?, 2, '0')
            ORDER BY secuencia_plaza, cve_empleado
            """, tableName);

        List<DescPlazasDatosPercepciones> raw =
                jdbcTemplate.query(sql, rowMapper, neyemp, quincena);

        if (raw.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron percepciones"
            );
        }

        List<DescPlazasDatosPercepcionesSimp> simp =
                mapper.toDatosPercepcionesSimpList(raw);

        return mapper.toPercepcionesPlazaDtoList(simp);
    }

}
