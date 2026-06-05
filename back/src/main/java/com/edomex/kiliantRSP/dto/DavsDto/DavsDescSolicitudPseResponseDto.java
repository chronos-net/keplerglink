package com.edomex.kiliantRSP.dto.DavsDto;

public record DavsDescSolicitudPseResponseDto(
        DavsDescSolicitudResponseDto encabezado,
        PseDetalleDto detalle
) {}
