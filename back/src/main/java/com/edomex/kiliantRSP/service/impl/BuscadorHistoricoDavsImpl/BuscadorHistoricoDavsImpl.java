package com.edomex.kiliantRSP.service.impl.BuscadorHistoricoDavsImpl;

import com.edomex.kiliantRSP.dto.BuscadorHistoricoDavsDto.BuscadorHistoricoDavsDto;
import com.edomex.kiliantRSP.service.BuscadorHistoricoDavsService.BuscadorHistoricoDavsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscadorHistoricoDavsImpl implements BuscadorHistoricoDavsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BuscadorHistoricoDavsDto> buscar(String termino) {

        String sql = """
            SELECT 
                cliente_o_proveedor,
                nombre_cliente
            FROM h_asesoria
            WHERE cliente_o_proveedor ILIKE ?
               OR nombre_cliente ILIKE ?
            ORDER BY nombre_cliente
            LIMIT 10
            
        """;

        String like = "%" + termino + "%";

        return jdbcTemplate.query(
                sql,
                new Object[]{like, like},
                (rs, rowNum) -> new BuscadorHistoricoDavsDto(
                        rs.getString("cliente_o_proveedor"),
                        rs.getString("nombre_cliente")
                )
        );
    }
}

