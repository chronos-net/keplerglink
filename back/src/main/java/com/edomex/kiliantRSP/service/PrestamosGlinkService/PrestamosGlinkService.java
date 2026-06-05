package com.edomex.kiliantRSP.service.PrestamosGlinkService;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkdto;
import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkCuerpo;
import com.edomex.kiliantRSP.service.impl.PrestamosGlinkImpl.PrestamosGlink;

public interface PrestamosGlinkService {
    PrestamosGlinkCuerpo creacionPrestamoGlink(PrestamosGlinkdto dto);
}
