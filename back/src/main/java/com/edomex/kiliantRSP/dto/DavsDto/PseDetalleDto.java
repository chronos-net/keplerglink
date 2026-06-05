package com.edomex.kiliantRSP.dto.DavsDto;

import java.util.List;

public record PseDetalleDto(
        List<PseImporteDto> importes,
        String mensaje
) {}
