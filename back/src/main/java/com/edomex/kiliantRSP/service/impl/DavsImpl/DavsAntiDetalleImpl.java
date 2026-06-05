package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsImporteDetalleDto;
import com.edomex.kiliantRSP.service.DavsService.DavsAntiDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DavsAntiDetalleImpl implements DavsAntiDetalleService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(DavsAntiDetalleImpl.class);

    private final RowMapper<Concepto> conceptoMapper = (rs, rowNum) ->
            new Concepto(
                    rs.getString("concepto"),
                    rs.getInt("anios_totales"),
                    rs.getInt("meses_totales")
            );

    @Override
    public DavsImporteDetalleDto obtenerAntiDetalle(Long cveKdm1) {

        try {

            String sqlTramite = """
                    SELECT
                    folio_documento AS folio,
                    a.monto_anticipos AS SUELDO_BASE,
                    a.saldo_documento AS  SUELDO_BASE_MENSUAL
                    FROM h_tramite a
                    WHERE cve_kdm1 = ?
                    """;

            var tramite = jdbcTemplate.queryForObject(
                    sqlTramite,
                    (rs, rowNum) -> new TramiteData(
                            rs.getString("folio"),
                            rs.getBigDecimal("SUELDO_BASE"),
                            rs.getBigDecimal("SUELDO_BASE_MENSUAL")
                    ),
                    cveKdm1
            );

            String sqlConceptos = """
                    SELECT
                    descripcion_del AS concepto,
                    descuento_porce_3 AS anios_totales,
                    ieps_porcentual AS meses_totales
                    FROM h_fecha
                    WHERE folio_documento = ?
                    """;

            List<Concepto> conceptos = jdbcTemplate.query(sqlConceptos, conceptoMapper, tramite.folio());

            int totalAnios = 0;
            int totalMeses = 0;

            for (Concepto c : conceptos) {

                if ("INGRESO".equals(c.concepto) || "REINGRESO".equals(c.concepto)) {
                    totalAnios += c.anios;
                    totalMeses += c.meses;
                }

                else if ("LICENCIAS".equals(c.concepto)) {
                    totalAnios -= c.anios;
                    totalMeses -= c.meses;
                }

                while (totalMeses < 0) {
                    totalMeses += 12;
                    totalAnios--;
                }

                while (totalMeses >= 12) {
                    totalMeses -= 12;
                    totalAnios++;
                }
            }

            BigDecimal importeSueldoBase =
                    tramite.sueldoBase()
                            .divide(BigDecimal.valueOf(30.4), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(12))
                            .multiply(BigDecimal.valueOf(totalAnios));

            BigDecimal importeSalarioMinimo = tramite.sueldoBaseMensual();

            BigDecimal importeAPagar = BigDecimal.ZERO;

            return new DavsImporteDetalleDto(
                    totalAnios,
                    totalMeses,
                    totalAnios,
                    importeSueldoBase,
                    importeSalarioMinimo,
                    importeAPagar
            );

        } catch (Exception e) {

            log.error("Error calculando antigüedad", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error calculando antigüedad del trámite"
            );
        }
    }

    private record Concepto(
            String concepto,
            int anios,
            int meses
    ) {}

    private record TramiteData(
            String folio,
            BigDecimal sueldoBase,
            BigDecimal sueldoBaseMensual
    ) {}
}