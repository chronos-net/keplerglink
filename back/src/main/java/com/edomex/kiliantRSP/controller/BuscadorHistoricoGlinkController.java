package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.service.BuscadorHistoricoGlinkService.BuscadorHistoricoGlinkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buscadorHistoricoGlink")
@Tag(name = "Buscador en Vivo de cabesera historico", description = "Controlador encargado de realisar una busqueda en vivo cpn las posibles opciones")
public class BuscadorHistoricoGlinkController {

    private final BuscadorHistoricoGlinkService vistaCabeseraHistoricoService;

    @GetMapping
    public ResponseEntity<?> buscarPorNeyemp(@RequestParam("q") String cvesp){
        var resultado = vistaCabeseraHistoricoService.buscadorPorNeyemp(cvesp);
        return ResponseEntity.ok(resultado);
    }
}