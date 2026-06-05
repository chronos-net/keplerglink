package com.edomex.kiliantRSP.service.DavsService;

import com.edomex.kiliantRSP.dto.DavsDto.DavsSolicitudDto;

import java.util.List;

public interface DavsHsolicitud {

    List<DavsSolicitudDto> obtenerSolicitud(String neyemp, String negnom);
}
