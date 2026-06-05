package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.AnioDto.AnioResponse;
import com.edomex.kiliantRSP.service.AnioService.AnioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/anio")
@Tag(name = "Anios", description = "Controlador encargado de mostrar el catalogo de anios que tiene el sistema")
public class AnioController {

    private final AnioService anioService;

    @GetMapping("/catalogo")
    @Operation(summary = "Muestra el catálogo de años que soporta el sistema")
    public AnioResponse obtenerAnios() {
        return new AnioResponse(anioService.obtenerDatosAnios());
    }

}
