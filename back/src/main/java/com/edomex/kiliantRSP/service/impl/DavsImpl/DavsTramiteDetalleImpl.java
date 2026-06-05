package com.edomex.kiliantRSP.service.impl.DavsImpl;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescTramiteDto;
import com.edomex.kiliantRSP.dto.DavsDto.DavsDescTramiteRDto;
import com.edomex.kiliantRSP.service.DavsService.DavsTramiteDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.sql.Date;

@Service
@RequiredArgsConstructor
public class DavsTramiteDetalleImpl implements DavsTramiteDetalleService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(DavsDescTramiteImpl.class);

    private final RowMapper<DavsDescTramiteRDto> rowMapper = (rs, rowNum) ->
            new DavsDescTramiteRDto(

                    rs.getString("unidadAdministrativa"),
                    rs.getString("folio"),
                    rs.getString("folioAsesoria"),
                    rs.getDate("fecha"),
                    rs.getDate("fechaBaja"),
                    rs.getTime("horaInicial") != null ? rs.getTime("horaInicial").toLocalTime() : null,
                    rs.getTime("horaFinal") != null ? rs.getTime("horaFinal").toLocalTime() : null,
                    rs.getString("csp"),
                    rs.getString("nombre"),
                    rs.getString("rfc"),
                    rs.getString("issemym"),
                    rs.getString("direccion"),
                    rs.getString("telefonoOficina"),
                    rs.getString("telefonoParticular"),
                    rs.getString("lugarDePago"),
                    rs.getString("tipoDeSindicato"),
                    rs.getString("motivoSeparacion"),
                    rs.getString("tipoSeguro"),
                    rs.getString("sueldoBase"),
                    rs.getString("quincena"),
                    rs.getString("subsecretaria"),
                    rs.getString("direccionGeneral"),
                    rs.getString("direccionDeArea"),
                    rs.getString("subdireccion"),
                    rs.getString("departamento"),
                    rs.getString("puesto"),
                    rs.getString("nivelRango"),
                    rs.getString("sueldoBaseMensual"),
                    rs.getString("primaDeAntiguedad"),
                    rs.getString("primaPorJubilacion"),
                    rs.getString("seguroDeVida"),
                    rs.getString("pagoDeBeneficiosIndividualesForemex"),
                    rs.getString("comentarios"),
                    rs.getLong("cveKdm1")
            );
    @Override
    public DavsDescTramiteDto obtenerDetalleTramite(Long cveKdm1){

        System.out.println("llave primaria : " + cveKdm1);

        String sql = """
                SELECT
                    sucursal AS unidadAdministrativa,
                    folio_documento AS folio,
                    folio_documento_a_anexar AS folioAsesoria,
                    fecha AS fecha,
                    fecha_pago AS fechaBaja,
                    hora_de_captura AS horaInicial,
                    hora_de_pago_o_entrega AS horaFinal,
                    cliente_o_proveedor AS csp,
                    nombre_cliente AS nombre,
                    registro_federal AS rfc,
                    clave_proyecto AS issemym,
                    calle_cliente AS direccion,
                    colonia_cliente AS telefonoOficina,
                    poblacion_cliente AS telefonoParticular,
                    clave_banco AS lugarDePago,
                    des_sindicato AS tipoDeSindicato,
                    destinatario_cheque AS motivoSeparacion,
                    almacen AS tipoSeguro,
                    monto_anticipos AS sueldoBase,
                    '' AS quincena,
                    '' AS subsecretaria,
                    '' AS direccionGeneral,
                    '' AS direccionDeArea,
                    referencia AS departamento,
                    '' AS subdireccion,
                    '' AS puesto,
                    '' AS nivelRango,
                    saldo_documento AS sueldoBaseMensual,
                    destinatario_cheque AS primaDeAntiguedad,
                    destinatario_cheque AS primaPorJubilacion,
                    destinatario_cheque AS seguroDeVida,
                    destinatario_cheque AS pagoDeBeneficiosIndividualesForemex,
                    CONCAT(comentario_1, comentario_2, comentario_3) AS comentarios,
                    cve_kdm1 AS cveKdm1
                FROM h_tramite
                WHERE cve_kdm1 = ?
                """;



        try {

            DavsDescTramiteRDto result = jdbcTemplate.queryForObject(sql,rowMapper,cveKdm1);

            String folio = result.folio();

            System.out.println("folio : " + folio);

            String sql2 = """
                    SELECT
                    descripcion_del AS concepto,
                    cantidad_de_uni AS dia_inicial,
                    unidad AS mes_inicial,
                    precio_unitario AS anio_inicial,
                    importe_de_la_p AS dia_final,
                    descuento_porce_1 AS mes_final,
                    descuento_porce_2 AS anio_final,
                    descuento_porce_3 AS anios_totales,
                    ieps_porcentual AS meses_totales
                    FROM h_fecha
                    WHERE folio_documento = ?
                """;

            String periodo = determinarPeriodo(
                    new Date(result.fechaBaja() != null
                            ? result.fechaBaja().getTime()
                            : System.currentTimeMillis())
            );



            String codigoDepto = result.departamento();

            String direccionArea = obtenerLeyenda(codigoDepto.substring(0,5) + "0000");
            String subdireccion = obtenerLeyenda(codigoDepto.substring(0,6) + "0000");
            String departamento = obtenerLeyenda(codigoDepto.substring(0,9));

            return new DavsDescTramiteDto(

                    result.unidadAdministrativa(),
                    result.folio(),
                    result.folioAsesoria(),
                    result.fecha(),
                    result.fechaBaja(),
                    result.horaInicial(),
                    result.horaFinal(),
                    result.csp(),
                    result.nombre(),
                    result.rfc(),
                    result.issemym(),
                    result.direccion(),
                    result.telefonoOficina(),
                    result.telefonoParticular(),
                    result.lugarDePago(),
                    result.tipoDeSindicato(),
                    result.motivoSeparacion(),
                    result.tipoSeguro(),
                    result.sueldoBase(),
                    periodo,
                    result.subsecretaria(),
                    result.direccionGeneral(),
                    direccionArea,
                    subdireccion,
                    departamento,
                    result.puesto(),
                    result.nivelRango(),
                    result.sueldoBaseMensual(),
                    result.primaDeAntiguedad(),
                    result.primaPorJubilacion(),
                    result.seguroDeVida(),
                    result.pagoDeBeneficiosIndividualesForemex(),
                    result.comentarios(),
                    result.cveKdm1()

            );

        } catch (Exception e) {

            log.error("Error ejecutando query DavsDescTramite", e);

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontro el registro de tramite"
            );
        }
    }

    private String determinarPeriodo(Date fecha) {

        if (fecha == null) {
            return "";
        }

        LocalDate localDate = fecha.toLocalDate();

        int dia = localDate.getDayOfMonth();
        int mes = localDate.getMonthValue();
        int anio = localDate.getYear();

        if (dia <= 15) {
            return "Primera Quincena del" + mes + "del" + anio;
        } else {
            return "Segunda Quincena del" + mes + "del" + anio;
        }
    }

    private String obtenerLeyenda(String cve9){

        String sql = """
                SELECT ley25
                FROM cat_adscripciones_9
                WHERE cve_9 = ?
                """;

        try{
            return jdbcTemplate.queryForObject(sql,String.class,cve9);
        }catch(Exception e){
            return "";
        }
    }
}
