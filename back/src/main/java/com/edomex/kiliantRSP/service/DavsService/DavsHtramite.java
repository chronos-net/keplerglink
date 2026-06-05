package com.edomex.kiliantRSP.service.DavsService;

import com.edomex.kiliantRSP.dto.DavsDto.DavsTramiteDto;

import java.util.List;

public interface DavsHtramite {

    List<DavsTramiteDto> obtenerTramite(String neyemp, String negnom);
}
