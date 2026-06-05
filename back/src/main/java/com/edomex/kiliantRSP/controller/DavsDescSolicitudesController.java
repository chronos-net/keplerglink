package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescSolicitudResponseDto;
import com.edomex.kiliantRSP.service.DavsService.DavsDescSolicitudesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/davs/solicitudes")
@RequiredArgsConstructor
public class DavsDescSolicitudesController {

    private final DavsDescSolicitudesService davsDescSolicitudesService;

    @GetMapping("/{cveKdm1}/{neyemp}/{destinatarioCheque}")
    public ResponseEntity<Object> obtenerDetalleSolicituf(
            @PathVariable Long cveKdm1,
            @PathVariable String neyemp,
            @PathVariable String destinatarioCheque
    ) {
        return ResponseEntity.ok(
                davsDescSolicitudesService.obtenerDescService(
                        cveKdm1,
                        neyemp,
                        destinatarioCheque
                )
        );
    }

}
