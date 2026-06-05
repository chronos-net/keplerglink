package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.Pensiondto;
import com.edomex.kiliantRSP.service.PensionService.PensionService;
import com.edomex.kiliantRSP.service.impl.PensionImpl.Pension;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pension")
@Tag(
        name = "Pensión",
        description = "Controlador encargado de verificar si el Servidor Público tiene pensiones activas"
)
public class PensionController {

    private final PensionService pensionService;

    @GetMapping("/consultar")
    @Operation(
            summary = "Consulta si un SP tiene pensiones",
            description = "Devuelve información de pensiones para el servidor público consultado."
    )
    public Pension obtenerPension(
            @Parameter(description = "Clave neyemp del servidor público", example = "820218461")
            @RequestParam String neyemp,

            @Parameter(description = "Periodo en formato de dos dígitos", example = "03")
            @RequestParam String periodo,

            @Parameter(description = "Año a consultar del 1996 hasta el 2022", example = "1996")
            @RequestParam int anio
    ) {
        var dto = new Pensiondto(neyemp, periodo, anio);
        return pensionService.getPension(dto);
    }
}
