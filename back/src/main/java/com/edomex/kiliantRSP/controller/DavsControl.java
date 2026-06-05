package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.DavsDto.Davsdto;
import com.edomex.kiliantRSP.service.DavsService.DavsService;
import com.edomex.kiliantRSP.service.impl.DavsImpl.Davs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/davs")
@Tag(name = "Davs Historico", description = "Muestra el historico de davs")
public class DavsControl {

    private final DavsService davsService;

    @PostMapping("/historico")
    @Operation(summary = "Obtiene el historico de DAVS")
    public ResponseEntity<?> creacionHistoricoDavs(@RequestBody Davsdto davsdto){

        try{
            Davs respuesta = davsService.getDavs(davsdto);

            return ResponseEntity.ok(respuesta);
        }catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body("Datos del servidor público no encontrados"); // 404

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error interno del servidor"); // 500
        }
    }
}

