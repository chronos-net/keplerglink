package com.edomex.kiliantRSP.dto.DavsDto;

import java.time.LocalTime;
import java.util.Date;

public record DavsDescAsesoriaRdto(

        String unidadAdminitrativo,
        String folio,
        Date fecha,
        LocalTime horaInicial,
        LocalTime horaFinal,
        String neyemp,
        String negnom,
        String rfc,
        String issemyn,
        String direccion,
        String telefonoOficina,
        String telefonoPArticular,
        String lugarPago,
        String sindicato,
        String quincena,
        String subSecretaria,
        String direccionGeneral,
        String direccionArea,
        String subdireccion,
        String departamento,
        String puesto,
        String nivelRango,
        String sueldoMensual,
        String primaAntiguedad,
        String primaJubilacio,
        String seguroVida,
        String pagosIndividualesForemex,
        String comentarios,
        Long cveKdm1
) {
}
