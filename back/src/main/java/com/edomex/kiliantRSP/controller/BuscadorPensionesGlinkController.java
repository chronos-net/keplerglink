package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.service.BuscadorPensionesGlinkService.BuscadorPensionesGlinkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buscadorPensionesGlink")
@Tag(name = "Buscador en vivo de los que tienen pensiones", description = "Controlador encargado de realisar una busqueda en vivo con las posibles opciones")
public class BuscadorPensionesGlinkController {

    private final BuscadorPensionesGlinkService buscadorPensionesGlinkService;

    @GetMapping
    public ResponseEntity<?> buscarPorNeyemp(@RequestParam("q") String clavesp){
        var resultado = buscadorPensionesGlinkService.buscarPorNeyemp(clavesp);
        return ResponseEntity.ok(resultado);
    }
}
