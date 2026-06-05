package com.edomex.kiliantRSP.service.impl.PensionesGlinkImpl;

import com.edomex.kiliantRSP.dto.PensionesGlinkDto.PensionesGlinkDto;
import com.edomex.kiliantRSP.dto.PensionesGlinkDto.PensionesGlinkResponse;
import com.edomex.kiliantRSP.service.PensionesGlinkService.PensionesGlinkService;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PensionesGlinkImpl implements PensionesGlinkService {

    private final JdbcTemplate jdbc;

    @Override
    public List<PensionesGlinkResponse> consultarPensiones(PensionesGlinkDto dto) {

        String sql = """
            SELECT clavesp, nombresp, rfc, fechain, nombrepension, tipo_desc,
                   altaqna, altano, porcentaje, importe, referencia
            FROM public.vista_pensiones_glink
            WHERE clavesp = ?
        """;

        var lista = jdbc.query(
                sql,
                new Object[]{dto.neyemp()},
                (rs, rowNum) -> new PensionesGlinkResponse(
                        rs.getString("clavesp"),
                        rs.getString("nombresp"),
                        rs.getString("rfc"),
                        rs.getString("fechain"),
                        rs.getString("nombrepension"),
                        rs.getString("tipo_desc"),
                        rs.getInt("altaqna"),
                        rs.getInt("altano"),
                        rs.getDouble("porcentaje"),
                        rs.getDouble("importe"),
                        rs.getString("referencia")
                )
        );

        return lista;
    }
}
