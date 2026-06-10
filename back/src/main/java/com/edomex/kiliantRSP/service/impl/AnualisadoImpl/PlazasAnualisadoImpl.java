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
    public List<PlazasAnualisadosDto> obtenerPlazasAnalisados(String neyemp, int anio) {
        String tableKddes = kddesResolver.obtenerTablaKddes(anio);
        String tableKdnom = "kdnom" + anio;

        String sql = String.format("""
                SELECT DISTINCT ON (h.periodo)
                        h.periodo,
                        h.plaza,
                        h.adsc AS ads,
                        n.cheque,
                        h.puesto AS categoria,
                        h.centro_trabajo AS lugarPago,
                        h.lugar_pago AS centroTrabajo,
                        NULL AS leyendaPuesto
                FROM %s h
                LEFT JOIN %s n
                  ON n.neyemp = h.neyemp
                 AND n.periodo = h.periodo
                WHERE h.neyemp = ?
                  AND CAST(h.secuencia_plaza AS INTEGER) < 30
                  AND TRIM(h.tipo_nomina::text) IN ('0', '1')
                ORDER BY h.periodo,
                         CAST(h.secuencia_plaza AS INTEGER) DESC,
                         h.cve_empleado DESC NULLS LAST
                """, tableKddes, tableKdnom);

        try {
            return jdbcTemplate.query(sql, rowMapper, neyemp);
        } catch (Exception e) {
            log.error("Error ejecutando query cabecera anualizado", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "plazas no encontradas",
                    e
            );
        }
    }
}
