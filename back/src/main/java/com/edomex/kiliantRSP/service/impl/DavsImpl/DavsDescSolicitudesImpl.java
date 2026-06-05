package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.*;
import com.edomex.kiliantRSP.service.DavsService.DavsDescSolicitudesService;
import com.edomex.kiliantRSP.service.DavsService.strategy.DestinatarioChequeStrategy;
import com.edomex.kiliantRSP.service.DavsService.strategy.DestinatarioChequeStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DavsDescSolicitudesImpl implements DavsDescSolicitudesService {

    private final JdbcTemplate jdbcTemplate;
    private final DestinatarioChequeStrategyFactory strategyFactory;

    private final RowMapper<DavsSolicitudApoyoDto> rowMapper = (rs, rowNum) ->
            new DavsSolicitudApoyoDto(
                    rs.getLong("cveKdm1"),
                    rs.getString("folioDocumento"),
                    rs.getString("tipoMovimiento"),
                    rs.getDate("fechaPago"),
                    rs.getInt("plazoDias"),
                    rs.getString("clienteProveedor"),
                    rs.getDate("fecha"),
                    rs.getBigDecimal("saldoDocumento"),
                    rs.getString("nombre"),
                    rs.getString("rfc")
            );

    @Override
    public Object obtenerDescService(
            Long cveKdm1,
            String neyemp,
            String destinatarioCheque
    ) {

        String sql = """
            SELECT
                a.cve_kdm1 AS cveKdm1,
                a.folio_documento AS folioDocumento,
                a.tipo_de_movimiento AS tipoMovimiento,
                a.fecha_pago AS fechaPago,
                a.plazo_en_dias AS plazoDias,
                a.cliente_o_proveedor AS clienteProveedor,
                a.fecha AS fecha,
                a.saldo_documento AS saldoDocumento,
                                a.nombre_cliente AS nombre,
                                a.registro_federal AS rfc
            FROM h_solicitud a
            WHERE cve_kdm1 = ?
        """;

        DavsSolicitudApoyoDto solicitud;

        try {
            solicitud = jdbcTemplate.queryForObject(sql, rowMapper, cveKdm1);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró el registro de la solicitud"
            );
        }

        DestinatarioChequeStrategy strategy =
                strategyFactory.getStrategy(destinatarioCheque);

        Object detalle = strategy.obtenerDetalle(solicitud);

        // 🔹 encabezado común
        DavsDescSolicitudResponseDto encabezado =
                new DavsDescSolicitudResponseDto(
                        solicitud.fecha(),
                        solicitud.rfc(), // RFC
                        solicitud.nombre(), // nombre
                        solicitud.saldoDocumento(),
                        solicitud.plazoDias(),
                        solicitud.fechaPago(),
                        obtenerMensaje(solicitud)
                );

// 🔥 CASO PSE
        // 🔥 CASO PSE
        if ("PSE".equals(strategy.getTipo())) {

            PseDetalleDto pse = (PseDetalleDto) detalle;

            DavsSolicitudFrontDto.Encabezado encabezadoFront =
                    new DavsSolicitudFrontDto.Encabezado(
                            solicitud.fecha(),
                            solicitud.rfc(),
                            solicitud.nombre()
                    );

            DavsSolicitudFrontDto.ResumenPago resumenPago =
                    new DavsSolicitudFrontDto.ResumenPago(
                            solicitud.saldoDocumento(),
                            ""
                    );

            DavsSolicitudFrontDto.Mensaje mensaje =
                    new DavsSolicitudFrontDto.Mensaje(
                            "paragraph",
                            encabezado.comentario()
                    );

            List<ConceptoDto> conceptos = pse.importes()
                    .stream()
                    .filter(i -> !"PSE".equals(i.tipoMovimiento().trim()))
                    .map(i -> new ConceptoDto(
                            i.tipoMovimiento(),
                            i.importe(),
                            i.importe2()
                    ))
                    .toList();

            List<ConceptoDto> movimientos = pse.importes()
                    .stream()
                    .filter(i -> "PSE".equals(i.tipoMovimiento().trim()))
                    .map(i -> new ConceptoDto(
                            i.tipoMovimiento(),
                            i.importe(),
                            i.importe2()
                    ))
                    .toList();

            return new DavsSolicitudFrontDto(

                    "SOLICITUD",
                    solicitud.tipoMovimiento(),
                    solicitud.folioDocumento(),
                    "ENTREGADO",

                    encabezadoFront,
                    resumenPago,
                    mensaje,

                    conceptos,
                    movimientos
            );
        }

        // 🔥 FRB u otros
        return encabezado;
    }

    private String obtenerMensaje(DavsSolicitudApoyoDto solicitud) {

        String sql = """
        SELECT CONCAT(
            comentario1,' ',
            comentario2,' ',
            comentario3,' ',
            comentario4
        )
        FROM kdaacattexto
        WHERE anio = '2008'
          AND prestacion = ?
          AND sindicato = ?
    """;

        String prestacion = solicitud.tipoMovimiento().substring(0,1);
        String sindicato = String.valueOf(solicitud.plazoDias());

        return jdbcTemplate.queryForObject(
                sql,
                String.class,
                prestacion,
                sindicato
        );
    }
}
