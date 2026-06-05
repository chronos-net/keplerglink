package com.edomex.kiliantRSP.service.impl.PensionImpl;

import com.edomex.kiliantRSP.dto.PensionDto.Pension2002_2022.Pension2002_2022;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionCabesera;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionItem;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.PensionDesc;
import com.edomex.kiliantRSP.service.PensionService.PensionHistoricoServiceDesc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PensionHistoricoServiceImplDesc implements PensionHistoricoServiceDesc {

    private final JdbcTemplate jdbcTemplate;

    /// mapeamos la cabesera
    private final RowMapper<PensionCabesera> pensionCabeseraRowMapper = (rs, rowMapper) -> new PensionCabesera(
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

    /// mapeamos las pensiones
    private final RowMapper<PensionItem>  pensionItemRowMapper = (rs, rowMapper) -> new PensionItem(
            rs.getString("negnom"),
            rs.getString("bco"),
            rs.getString("lugar_pago"),
            rs.getString("cantidad"),
            rs.getString("cheque"),
            rs.getString("n_cuenta")
    );

    /// metodo principal
    @Override
    public PensionDesc obtenerPensionDesc(String neyemp, String periodo, int anio){

        // ✅ Validar año
        if (String.valueOf(anio).length() != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Año inválido");
        }

        // ✅ Formato del periodo a 2 dígitos
        String periodoStr = String.format("%02d", Integer.parseInt(periodo.trim()));

        /// consulta cabesera
        String tabalaVista = "vw_kddes_con_detalles" + anio;
        String tablaNom = "kdnom" + anio;
        String tablaPension = "kdpension" + anio;

        String sqlCabecera = String.format("""
                SELECT
                    a.periodo,a.anio,
                    a.neyemp,a.adsc,
                    a.descripcion AS leyenda_adscripcion,a.cheque,
                    a.puesto,a.leyenda_puesto,
                    SUM(b.percep) AS percep,SUM(b.ded) AS ded,
                    SUM(b.percep - b.ded) AS neto,b.lug_pago,b.num_cuenta
                FROM %s a 
                LEFT JOIN %s b ON a.neyemp = b.neyemp AND a.periodo = b.periodo
                INNER JOIN %s c ON a.neyemp = c.neyemp AND a.periodo = c.periodo
                WHERE a.neyemp = ? AND a.periodo = ?
                GROUP BY
                    a.periodo, a.anio, a.neyemp, a.adsc, a.descripcion,
                    a.cheque, a.puesto, a.leyenda_puesto,
                    b.lug_pago, b.num_cuenta
                LIMIT 1
                """, tabalaVista, tablaNom, tablaPension);

        PensionCabesera cabesera;

        try{
            cabesera = jdbcTemplate.queryForObject(sqlCabecera, pensionCabeseraRowMapper, neyemp.trim(), periodoStr);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este servidor publico no cuenta con pensiones", e);
        }

        String sqlPension = String.format("""
                SELECT 
                    negnom, banco as bco, 
                    lugar_pago, neto as cantidad, 
                    cheque, n_cuenta
                FROM %s
                WHERE neyemp = ? and periodo = ?
                """, tablaPension);

        List<PensionItem> pensionItems;

        try{
            pensionItems = jdbcTemplate.query(sqlPension, pensionItemRowMapper, neyemp.trim(), periodo);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ninguna pension", e);
        }

        return new PensionDesc(cabesera, pensionItems);
    }
}
