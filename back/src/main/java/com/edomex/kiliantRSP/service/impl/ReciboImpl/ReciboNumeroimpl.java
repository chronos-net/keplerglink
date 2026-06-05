package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.service.ReciboService.ReciboNumeroService;
import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboNumeroDto;
import com.edomex.kiliantRSP.util.KdnomBancoResolver;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class ReciboNumeroimpl implements ReciboNumeroService{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReciboNumeroDto> rowMapper = (rs, rowNum) -> {

        String numCuenta = rs.getString("numCuenta");
        String bancoCodigo = rs.getString("banco");
        String bancoFinal = KdnomBancoResolver.resolverNombreBanco(bancoCodigo, numCuenta);

        return new ReciboNumeroDto(
                rs.getString("numRecibo"),
                bancoFinal,      // nombre del banco
                bancoCodigo,     // código original
                numCuenta,
                rs.getString("lugarPago")
        );
    };



    @Override
    public ReciboNumeroDto obtenerNumero(String neyemp, int anio, String quincena){

        String tableName = "kdnom" + anio;

        String sql = String.format("""
        SELECT 
            a.num_recibo AS numRecibo,
            a.banco,
            a.num_cuenta AS numCuenta,
            a.lug_pago AS lugarPago
        FROM %s a
        WHERE a.neyemp = ? AND a.periodo = ?
        """, tableName);


        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "no encontramso el numero de recibo",
                    e
            );
        }
    }
}
