package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.Anualiasadodto;
import com.edomex.kiliantRSP.service.AnualisadoService.AnualisadoService;
import com.edomex.kiliantRSP.service.impl.AnualisadoImpl.Anualisado;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/anualisado")
@Tag(name = "Anualisado", description = "Controlador encargado de crear el anualisado")
public class AnualisadoController {

    private final AnualisadoService anualisadoService;

    @PostMapping("/validacionAnualisado")
    public ResponseEntity<?> creacionReciboKiliant(@RequestBody Anualiasadodto kdrhempdto) {
        try {

             Anualisado anualisado = anualisadoService.getAnualisado(kdrhempdto);

             return ResponseEntity.ok(anualisado);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body("Datos del servidor publico no encontrados");

        } catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .body("Error interno del servidor");
        }
    }
}
