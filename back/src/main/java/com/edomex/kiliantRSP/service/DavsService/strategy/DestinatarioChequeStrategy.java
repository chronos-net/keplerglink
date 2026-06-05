package com.edomex.kiliantRSP.service.DavsService.strategy;

import com.edomex.kiliantRSP.dto.DavsDto.DavsSolicitudApoyoDto;

public interface DestinatarioChequeStrategy {

    Object obtenerDetalle(DavsSolicitudApoyoDto solicitudBase);

    String getTipo();
}
