package com.edomex.kiliantRSP.service.PrestamosGlinkService;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkDesc;

import java.util.List;

public interface PrestamosGlinkDescService {
    List<PrestamosGlinkDesc> obtenerDescripciones(String neyemp);
}
