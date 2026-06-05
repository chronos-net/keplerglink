package com.edomex.kiliantRSP.service.helpers.Recibos;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboConceptoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReciboCatalogoHelper {

    private final JdbcTemplate jdbcTemplate;

    public List<ReciboConceptoDto> enriquecerConDescripcion(
            List<ReciboConceptoDto> conceptos,
            int anio
    ) {

        if (conceptos == null || conceptos.isEmpty()) {
            return conceptos;
        }

        Set<String> codigos = conceptos.stream()
                .map(ReciboConceptoDto::codigo)
                .filter(c -> c != null && !c.isBlank())
                .collect(Collectors.toSet());

        if (codigos.isEmpty()) {
            return conceptos;
        }

        String inSql = codigos.stream()
                .map(c -> "?")
                .collect(Collectors.joining(","));

        String tablaCatalogo = "kdnmctgcon" + anio;

        String sql = """
                SELECT clave, descripcion
                FROM %s
                WHERE clave IN (%s)
                """.formatted(tablaCatalogo, inSql);

        Map<String, String> mapaDesc = jdbcTemplate.query(
                sql,
                codigos.toArray(),
                rs -> {
                    Map<String, String> map = new HashMap<>();
                    while (rs.next()) {
                        map.put(
                                rs.getString("clave"),
                                rs.getString("descripcion")
                        );
                    }
                    return map;
                }
        );

        return conceptos.stream()
                .map(c -> new ReciboConceptoDto(
                        c.codigo(),
                        mapaDesc.getOrDefault(c.codigo(), ""),
                        c.importe()
                ))
                .toList();
    }
}