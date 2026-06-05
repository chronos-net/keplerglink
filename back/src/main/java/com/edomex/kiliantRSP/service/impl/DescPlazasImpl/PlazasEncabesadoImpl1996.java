package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DesglosePlazasEncabesado1996Dto;
import com.edomex.kiliantRSP.service.DescPlazasService.PlazasEncabesadoService1996;
import com.edomex.kiliantRSP.util.KdnomBancoResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlazasEncabesadoImpl1996 implements PlazasEncabesadoService1996{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DesglosePlazasEncabesado1996Dto> rowMapper = (rs, rowNum) -> {
        String numCuenta = rs.getString("numCuenta");
        String bancoCodigo = rs.getString("bancoCodigo");
        return new DesglosePlazasEncabesado1996Dto(
                rs.getString("neyemp"),
                rs.getString("negnom"),
                rs.getString("rfc"),
                rs.getString("cheque"),
                rs.getString("ads"),
                KdnomBancoResolver.resolverNombreBanco(bancoCodigo, numCuenta),
                numCuenta,
                rs.getString("numRecibo"),
                rs.getString("lugPago")
        );
    };

    @Override
    public DesglosePlazasEncabesado1996Dto obtnerEncabesadoPlaza(String neyemp, int anio, String quincena) {
        String tablePrincipal =     "kdrhemp";
        String tableSecundaria =    "kdnom" + anio;
        String tableTerciario =     "kddesx" + anio;

        String sql = String.format("""
                SELECT
                    a.clave_del_empledo AS neyemp,
                    a.nombre_completo AS negnom,
                    a.rfc,
                    b.cheque,
                    c.ads,
                    b.banco AS bancoCodigo,
                    b.num_cuenta AS numCuenta,
                    b.num_recibo AS numRecibo,
                    b.lug_pago AS lugPago
                FROM %s a
                LEFT JOIN %s b ON a.clave_del_empledo = b.neyemp
                LEFT JOIN %s c ON a.clave_del_empledo = c.neyemp
                WHERE a.clave_del_empledo = ? AND b.periodo = ? AND c.periodo = ? LIMIT 1;
                """, tablePrincipal, tableSecundaria, tableTerciario);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena, quincena);
        }catch (Exception e){
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron plazas para este servidor",
                    e
            );
        }
    }
}
