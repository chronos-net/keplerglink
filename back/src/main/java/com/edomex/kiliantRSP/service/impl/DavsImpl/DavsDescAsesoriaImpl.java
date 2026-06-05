package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescAsesoriaDto;
import com.edomex.kiliantRSP.dto.DavsDto.DavsDescAsesoriaRdto;
import com.edomex.kiliantRSP.service.DavsService.DavsDescAsesoriaService;
import com.edomex.kiliantRSP.service.impl.ReciboImpl.ReciboMultiPlazasImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class DavsDescAsesoriaImpl implements DavsDescAsesoriaService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ReciboMultiPlazasImpl.class);

    private final RowMapper<DavsDescAsesoriaRdto> rowMapper = (rs, rowNum) ->
            new DavsDescAsesoriaRdto(
                    rs.getString("unidadAdminitrativo"),
                    rs.getString("folio"),
                    rs.getDate("fecha"),
                    rs.getTime("horaInicial").toLocalTime(),
                    rs.getTime("horaFinal").toLocalTime(),
                    rs.getString("neyemp"),
                    rs.getString("negnom"),
                    rs.getString("rfc"),
                    rs.getString("issemyn"),
                    rs.getString("direccion"),
                    rs.getString("telefonoOficina"),
                    rs.getString("telefonoPArticular"),
                    rs.getString("lugarPago"),
                    rs.getString("sindicato"),
                    rs.getString("quincena"),
                    rs.getString("subSecretaria"),
                    rs.getString("direccionGeneral"),
                    rs.getString("direccionArea"),
                    rs.getString("subdireccion"),
                    rs.getString("departamento"),
                    rs.getString("puesto"),
                    rs.getString("nivelRango"),
                    rs.getString("sueldoMensual"),
                    rs.getString("primaAntiguedad"),
                    rs.getString("primaJubilacio"),
                    rs.getString("seguroVida"),
                    rs.getString("pagosIndividualesForemex"),
                    rs.getString("comentarios"),
                    rs.getLong("cveKdm1")
            );

    @Override
    public DavsDescAsesoriaDto obtenerDescAsesoria(Long cveKdm1, String neyemp) {

        System.out.println(cveKdm1);
        System.out.println("-------------");
        System.out.println(neyemp);

        String sql = String.format("""
                SELECT
                    sucursal AS unidadAdminitrativo,
                    folio_documento AS folio,
                    fecha,
                    hora_de_captura AS horaInicial,
                    hora_de_pago_o_entrega AS horaFinal,
                    cliente_o_proveedor AS neyemp,
                    nombre_cliente AS negnom,
                    registro_federal AS rfc,
                    clave_proyecto AS issemyn,
                    calle_cliente AS direccion,
                    colonia_cliente AS telefonoOficina,
                    '' AS telefonoPArticular,
                    '' AS lugarPago,
                    des_sindicato AS sindicato,
                    '' AS quincena,
                    '' AS subSecretaria,
                    '' AS direccionGeneral,
                    '' AS direccionArea,
                    '' AS subdireccion,
                    referencia AS departamento,
                    '' AS puesto,
                    '' AS nivelRango,
                    saldo_documento AS sueldoMensual,
                    destinatario_cheque AS primaAntiguedad,
                    destinatario_cheque AS primaJubilacio,
                    destinatario_cheque AS seguroVida,
                    destinatario_cheque AS pagosIndividualesForemex,
                    CONCAT(comentario_1, comentario_2, comentario_3) AS comentarios,
                    a.cve_kdm1 as cveKdm1
                FROM h_asesoria a
                WHERE a.cve_kdm1 = ?
                  AND a.cliente_o_proveedor = ?
                """);

        try {
            DavsDescAsesoriaRdto result =
                    jdbcTemplate.queryForObject(sql, rowMapper, cveKdm1, neyemp);

            // 🔁 Mapeo a DTO público
            return new DavsDescAsesoriaDto(
                    result.unidadAdminitrativo(),
                    result.folio(),
                    result.fecha(),
                    result.horaInicial(),
                    result.horaFinal(),
                    result.neyemp(),
                    result.negnom(),
                    result.rfc(),
                    result.issemyn(),
                    result.direccion(),
                    result.telefonoOficina(),
                    result.telefonoPArticular(),
                    result.lugarPago(),
                    result.sindicato(),
                    result.quincena(),
                    result.subSecretaria(),
                    result.direccionGeneral(),
                    result.direccionArea(),
                    result.subdireccion(),
                    result.departamento(),
                    result.puesto(),
                    result.nivelRango(),
                    result.sueldoMensual(),
                    result.primaAntiguedad(),
                    result.primaJubilacio(),
                    result.seguroVida(),
                    result.pagosIndividualesForemex(),
                    result.comentarios(),
                    result.cveKdm1()
            );

        } catch (DataAccessException e) {
            log.error("Error ejecutando query PlazasBase1996", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró la asesoría solicitada"
            );
        }
    }
}
