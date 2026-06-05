package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DeduccionesPlazaDto;
import com.edomex.kiliantRSP.service.DescPlazasService.Deducciones1996Service;
import com.edomex.kiliantRSP.Mapper.DescPlazasMapper.DatosDeduccionesPlazasMapper;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeducciones;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeduccionesSimp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Deducciones1996Impl implements Deducciones1996Service {

    private final JdbcTemplate jdbcTemplate;
    private final DatosDeduccionesPlazasMapper mapper;

    private final RowMapper<DescPlazasDatosDeducciones>  rowMapper = (rs, row) -> new DescPlazasDatosDeducciones(
            rs.getString("secuencia_plaza"),
            rs.getBigDecimal("total"),
            rs.getString("ded1"), rs.getString("ded2"), rs.getString("ded3"), rs.getString("ded4"), rs.getString("ded5"),
            rs.getString("ded6"), rs.getString("ded7"), rs.getString("ded8"), rs.getString("ded9"), rs.getString("ded10"),
            rs.getBigDecimal("imp1"), rs.getBigDecimal("imp2"), rs.getBigDecimal("imp3"), rs.getBigDecimal("imp4"), rs.getBigDecimal("imp5"),
            rs.getBigDecimal("imp6"), rs.getBigDecimal("Imp7"), rs.getBigDecimal("Imp8"), rs.getBigDecimal("Imp9"), rs.getBigDecimal("Imp10"),

            rs.getString("ded1A"), rs.getString("ded2A"), rs.getString("ded3A"), rs.getString("ded4A"), rs.getString("ded5A"),
            rs.getString("ded6A"), rs.getString("ded7A"), rs.getString("ded8A"), rs.getString("ded9A"), rs.getString("ded10A"),
            rs.getString("ded11A"), rs.getString("ded12A"), rs.getString("ded13A"), rs.getString("ded14A"), rs.getString("ded15A"),
            rs.getString("ded16A"), rs.getString("ded17A"), rs.getString("ded18A"), rs.getString("ded19A"), rs.getString("ded20A"),

            rs.getBigDecimal("imp1A"), rs.getBigDecimal("imp2A"), rs.getBigDecimal("imp3A"), rs.getBigDecimal("imp4A"), rs.getBigDecimal("imp5A"),
            rs.getBigDecimal("imp6A"), rs.getBigDecimal("Imp7A"), rs.getBigDecimal("Imp8A"), rs.getBigDecimal("Imp9A"), rs.getBigDecimal("Imp10A"),
            rs.getBigDecimal("imp11A"), rs.getBigDecimal("imp12A"), rs.getBigDecimal("imp13A"), rs.getBigDecimal("imp14A"), rs.getBigDecimal("imp15A"),
            rs.getBigDecimal("imp16A"), rs.getBigDecimal("Imp17A"), rs.getBigDecimal("Imp18A"), rs.getBigDecimal("Imp19A"), rs.getBigDecimal("Imp20A"),

            rs.getString("ded1B"), rs.getString("ded2B"), rs.getString("ded3B"), rs.getString("ded4B"), rs.getString("ded5B"),
            rs.getString("ded6B"), rs.getString("ded7B"), rs.getString("ded8B"), rs.getString("ded9B"), rs.getString("ded10B"),
            rs.getString("ded11B"), rs.getString("ded12B"), rs.getString("ded13B"), rs.getString("ded14B"), rs.getString("ded15B"),
            rs.getString("ded16B"), rs.getString("ded17B"), rs.getString("ded18B"), rs.getString("ded19B"), rs.getString("ded20B"),

            rs.getBigDecimal("imp1B"), rs.getBigDecimal("imp2B"), rs.getBigDecimal("imp3B"), rs.getBigDecimal("imp4B"), rs.getBigDecimal("imp5B"),
            rs.getBigDecimal("imp6B"), rs.getBigDecimal("Imp7B"), rs.getBigDecimal("Imp8B"), rs.getBigDecimal("Imp9B"), rs.getBigDecimal("Imp10B"),
            rs.getBigDecimal("imp11B"), rs.getBigDecimal("imp12B"), rs.getBigDecimal("imp13B"), rs.getBigDecimal("imp14B"), rs.getBigDecimal("imp15B"),
            rs.getBigDecimal("imp16B"), rs.getBigDecimal("Imp17B"), rs.getBigDecimal("Imp18B"), rs.getBigDecimal("Imp19B"), rs.getBigDecimal("Imp20B")
    );

    @Override
    public List<DeduccionesPlazaDto> obtenerDeducciones(
            String neyemp,
            int anio,
            String quincena
    ){

        String tableName = "vista_deducciones_" + anio;

        String sql = String.format("""
            SELECT *
            FROM %s
            WHERE neyemp = ?
            AND periodo = LPAD(?, 2, '0')
            ORDER BY secuencia_plaza, cve_neyemp
            """, tableName);

        List<DescPlazasDatosDeducciones> raw =
                jdbcTemplate.query(sql, rowMapper, neyemp, quincena);

        if (raw.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron deducciones"
            );
        }

        List<DescPlazasDatosDeduccionesSimp> simp =
                mapper.toDatosDeduccionesSimpList(raw);

        return mapper.toDeduccionesPlazaDtoList(simp);
    }

}
