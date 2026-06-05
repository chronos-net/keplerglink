package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DeduccionesPlazaDto;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.PercepcionesPlazaDto;

import java.math.BigDecimal;

public record PlazasDto1996(
        String plazaId,
        String secuenciaPlaza,
        String puesto,
        String leyendaPuesto,
        String lugpago,
        String centroTrabajo,

        PercepcionesPlazaDto percepciones,
        DeduccionesPlazaDto deducciones,

        BigDecimal neto
){}
