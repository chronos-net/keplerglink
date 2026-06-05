package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.PeriodoDto.PeriodoResponse;
import com.edomex.kiliantRSP.service.PeriodoService.PeriodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/periodo")
@Tag(name = "Periodos", description = "Contorlador encargado de mostar el catalogo de periodos que tiene el sistema")
public class PeriodoController {

    private final PeriodoService periodoService;

    @GetMapping("/catalogo")
    @Operation(summary = "Muestra e catalogo de periodos que soporta el sistema")
    public PeriodoResponse obtnerPeriodos(){
        return new PeriodoResponse(periodoService.obtenerPeriodos());
    }
}
