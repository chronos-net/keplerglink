package com.edomex.kiliantRSP.service.DavsService;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescSolicitudPseResponseDto;

public interface DavsDescSolicitudesService {

    Object obtenerDescService(
            Long cveKdm1,
            String neyemp,
            String destinatarioCheque
    );
}
