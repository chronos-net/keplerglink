package com.edomex.kiliantRSP.dto.DavsDto;

import java.time.LocalTime;
import java.util.Date;

public record DavsDescTramiteDto(

        String unidadAdministrativa,
        String folio,
        String folioAsesoria,
        Date fecha,
        Date fechaBaja,
        LocalTime horaInicial,
        LocalTime horaFinal,
        String csp,
        String nombre,
        String rfc,
        String issemym,
        String direccion,
        String telefonoOficina,
        String telefonoParticular,
        String lugarDePago,
        String tipoDeSindicato,
        String motivoSeparacion,
        String tipoSeguro,
        String sueldoBase,
        String quincena,
        String subsecretaria,
        String direccionGeneral,
        String direccionDeArea,
        String subdireccion,
        String departamento,
        String puesto,
        String nivelRango,
        String sueldoBaseMensual,
        String primaDeAntiguedad,
        String primaPorJubilacion,
        String seguroDeVida,
        String pagoDeBeneficiosIndividualesForemex,
        String comentarios,
        Long cveKdm1

) {}