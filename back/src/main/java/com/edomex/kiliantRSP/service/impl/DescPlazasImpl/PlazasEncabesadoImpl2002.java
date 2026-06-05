package com.edomex.kiliantRSP.service.impl.DescPlazasImpl;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DesglosePlazasEncabesado2002Dto;
import com.edomex.kiliantRSP.service.DescPlazasService.PlazasEncabesadoService2002;
import com.edomex.kiliantRSP.util.KdnomBancoResolver;
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
public class PlazasEncabesadoImpl2002 implements PlazasEncabesadoService2002 {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(PlazasBase1996Impl.class);

    private final RowMapper<DesglosePlazasEncabesado2002Dto> rowMapper = (rs, rowNum) -> {
        String numCuenta = rs.getString("numCuenta");
        String bancoCodigo = rs.getString("bancoCodigo");
        return new DesglosePlazasEncabesado2002Dto(
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
    public DesglosePlazasEncabesado2002Dto obtenerEncabezadoPlazas(String neyemp, int anio, String quincena){
        String tablePrincipal =     "kdrhemp";
        String tableSecundaria =    "kdnom" + anio;
        String tableTerciario =     "kddes" + anio;

        String sql = String.format("""
                SELECT
                    a.clave_del_empledo AS neyemp,
                    a.nombre_completo AS negnom,
                    a.rfc,
                    b.cheque,
                    c.adsc AS ads,
                    b.banco AS bancoCodigo,
                    b.num_cuenta AS numCuenta,
                    b.num_recibo AS numRecibo,
                    b.lug_pago AS lugPago
                FROM %s a
                LEFT JOIN %s b ON a.clave_del_empledo = b.neyemp
                LEFT JOIN %s c ON a.clave_del_empledo = c.neyemp
                WHERE a.clave_del_empledo = ? AND b.periodo = ? AND c.periodo = ? LIMIT 1
                """, tablePrincipal, tableSecundaria, tableTerciario);

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, neyemp, quincena, quincena);
        }catch (Exception e){
            log.error("Error ejecutando query PlazasBase1996", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error consultando plazas base",
                    e
            );
        }
    }
}
