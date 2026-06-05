package com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes;

import com.edomex.kiliantRSP.service.impl.PrestamosGlinkImpl.PrestamosGlink;
import java.util.List;

public record PrestamosGlinkCuerpo(
        PrestamosGlinkCabesera cabeseraPrestamos,
        List<PrestamosGlinkDesc> descPrestamos
) implements PrestamosGlink{
}
