package com.edomex.kiliantRSP.service.BuscadorPrestamamosGlinkService;

import com.edomex.kiliantRSP.dto.BuscadorPrestamosGlinkDto.BuscadorPrestamosGlinkdto;

import java.util.List;

public interface BuscadorPrestamosGlinkService {
    List<BuscadorPrestamosGlinkdto> buscadorPorNeyemp(String clavesp);
}
