package com.edomex.kiliantRSP.service.impl.PensionImpl;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionCabeseraSimp;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionItem;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionSimp;
import com.edomex.kiliantRSP.service.PensionService.PensionHistoricoServiceSimp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PensionHistoricoServiceImplSimp implements PensionHistoricoServiceSimp {

    private final JdbcTemplate jdbcTemplate;

    // Mapeo de la cabecera
    private final RowMapper<PensionCabeseraSimp> pensionCabeseraRowMapper = (rs, rowMapper) -> new PensionCabeseraSimp(
            rs.getString("periodo"),
            rs.getString("anio"),
            rs.getString("neyemp"),
            rs.getString("adsc"),
            rs.getString("leyenda_adscripcion"),
            rs.getString("cheque"),
            rs.getString("puesto"),
            rs.getString("leyenda_puesto"),
            rs.getString("percep"),
            rs.getString("ded"),
            rs.getString("neto"),
            rs.getString("lug_pago"),
            rs.getString("num_cuenta")
    );

    // Mapeo de los ítems
    private final RowMapper<PensionItem> pensionItemRowMapper = (rs, rowMapper) -> new PensionItem(
            rs.getString("negnom"),
            rs.getString("bco"),
            rs.getString("lugar_pago"),
            rs.getString("cantidad"),
            rs.getString("cheque"),
            rs.getString("n_cuenta")
    );

    @Override
    public PensionSimp obtenerPensionSimp(String neyemp, String periodo, int anio)
    {
        // Validar año
        if (String.valueOf(anio).length() != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Año inválido");
        }

        String periodoStr = String.format("%02d", Integer.parseInt(periodo.trim()));

        String tablaName = "Kddesx" + anio;
        String tablaNom = "kdnom" + anio;
        String tablaPension = "kdpension" + anio;

        String sqlCabesera = String.format("""
                SELECT 
                    a.periodo,
                    a.anio,
                    a.neyemp,
                    a.ads AS adsc,
                    'NO CUENTA' AS leyenda_adscripcion,
                    b.cheque,
                    a.puesto,
                    'NO CUENTA' AS leyenda_puesto,
                    b.percep,
                    b.ded,
                    SUM(b.percep - b.ded) AS neto,
                    b.lug_pago,
                    b.num_cuenta
                FROM %s a
                    LEFT JOIN %s b ON a.neyemp = b.neyemp AND a.periodo = b.periodo
                    INNER JOIN %s c ON a.neyemp = c.neyemp AND a.periodo = c.periodo
                WHERE a.neyemp = ? AND a.periodo = ?    
                GROUP BY
                    a.periodo, a.anio, a.neyemp,
                    a.ads, a.puesto, b.percep,
                    b.ded, b.lug_pago, b.cheque, b.num_cuenta
                LIMIT 1
                """, tablaName, tablaNom, tablaPension);

        PensionCabeseraSimp cabesera;

        try {
            cabesera = jdbcTemplate.queryForObject(sqlCabesera, pensionCabeseraRowMapper, neyemp.trim(), periodoStr);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este servidor público no cuenta con pensiones", e);
        }

        String sqlPension = String.format("""
                SELECT 
                    negnom, banco AS bco, 
                    lugar_pago, neto AS cantidad, 
                    cheque, n_cuenta
                FROM %s
                WHERE neyemp = ? AND periodo = ?
                """, tablaPension);

        List<PensionItem> pensionItems;

        try {
            pensionItems = jdbcTemplate.query(sqlPension, pensionItemRowMapper, neyemp.trim(), periodoStr);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron pensiones adicionales", e);
        }

        return new PensionSimp(cabesera, pensionItems);
    }
}
