package com.edomex.kiliantRSP.dto.DavsDto;

public record DavsFechaDto(
        String concepto,
        String diaInicial,
        String mesInicial,
        String anioInicial,
        String diaFinal,
        int mesFinal,
        int anioFinal,
        int aniosTotales,
        int mesesTotales
) {
}
