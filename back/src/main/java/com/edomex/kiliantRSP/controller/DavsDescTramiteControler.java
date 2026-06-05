package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescTramiteDto;
import com.edomex.kiliantRSP.dto.DavsDto.DavsDetalleCompletoDto;
import com.edomex.kiliantRSP.service.DavsService.DavsDescTramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/davs/tramite")
@RequiredArgsConstructor
public class DavsDescTramiteControler {

    private final DavsDescTramiteService davsDescTramiteService;

    @GetMapping("/{cveKdm1}")
    public ResponseEntity<DavsDetalleCompletoDto> obtenerDetalleTramite(
            @PathVariable Long cveKdm1
    ){
        return ResponseEntity.ok(
                davsDescTramiteService.obtenerDetalle(cveKdm1)
        );
    }
}
