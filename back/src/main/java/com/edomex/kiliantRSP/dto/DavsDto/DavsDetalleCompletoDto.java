package com.edomex.kiliantRSP.dto.DavsDto;

public record DavsDetalleCompletoDto(

        DavsDescTramiteDto tramite,
        DavsFechaDto fecha,
        DavsImporteDetalleDto antiguedad
) {
}
