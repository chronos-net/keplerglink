package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.PlazasAnualisadosDto;
import com.edomex.kiliantRSP.service.AnualisadoService.PlazasAnualisadoService;
import com.edomex.kiliantRSP.service.helpers.Anualisados.KddesAnualizadoResolver;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.PlazasBase1996Impl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlazasAnualisadoImpl implements PlazasAnualisadoService {

    private final JdbcTemplate jdbcTemplate;
    private final KddesAnualizadoResolver kddesResolver;
    private static final Logger log = LoggerFactory.getLogger(PlazasBase1996Impl.class);

    private final RowMapper<PlazasAnualisadosDto> rowMapper = (rs, rowNum) -> new PlazasAnualisadosDto(
            rs.getString("periodo"),
            rs.getString("plaza"),
            rs.getString("ads"),
            rs.getString("cheque"),
            rs.getString("categoria"),
            rs.getString("lugarPago"),
            rs.getString("centroTrabajo"),
            rs.getString("leyendaPuesto")
    );

    @Override
    public List<PlazasAnualisadosDto> obtenerPlazasAnalisados(String neyemp, int anio){
        var fuente = kddesResolver.resolver(neyemp, anio);
        String tablePrimaria = fuente.tabla();
        String columnaSecuencia = fuente.columnaSecuencia();
        String tableSegundo = "kdnom" + anio;

        boolean usaKddesx = tablePrimaria.startsWith("kddesx");

        String columnaAds = usaKddesx ? "a.ads AS ads" : "a.adsc AS ads";
        String columnaCentroTrabajo = usaKddesx ? "a.clave_lugar AS centroTrabajo" : "a.lugar_pago AS centroTrabajo";
        String columnaLugarPago = usaKddesx ? "a.clave_centro AS lugarPago" : "a.centro_trabajo AS lugarPago";

        String sql = String.format("""
                SELECT DISTINCT ON (a.periodo)
                        a.periodo,
                        a.plaza,
                        %s,
                        b.cheque,
                        a.puesto AS categoria,
                        %s,
                        %s,
                        NULL AS leyendaPuesto
                FROM %s a 
                LEFT JOIN %s b ON a.neyemp = b.neyemp AND a.periodo = b.periodo
                WHERE a.neyemp = ?
                  AND TRIM(a.tipo_nomina::text) IN ('0', '1')
                ORDER BY a.periodo, a.%s DESC;
                """, columnaAds, columnaCentroTrabajo, columnaLugarPago, tablePrimaria, tableSegundo, columnaSecuencia);

        try {
            return jdbcTemplate.query(sql, rowMapper, neyemp);
        } catch (Exception e){
            log.error("Error ejecutando query PlazasBase1996", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "plazas no encontradas",
                    e
            );
        }
    }
}
