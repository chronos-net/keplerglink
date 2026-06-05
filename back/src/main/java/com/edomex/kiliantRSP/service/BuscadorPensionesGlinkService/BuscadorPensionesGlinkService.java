package com.edomex.kiliantRSP.service.BuscadorPensionesGlinkService;

import com.edomex.kiliantRSP.dto.BuscadorPensionesGlinkDto.BuscadorPensionesGlinkdto;

import java.util.List;

public interface BuscadorPensionesGlinkService {
    List<BuscadorPensionesGlinkdto> buscarPorNeyemp(String clavesp);
}

