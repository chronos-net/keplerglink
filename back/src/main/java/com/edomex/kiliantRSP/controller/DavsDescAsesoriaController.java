package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescAsesoriaDto;
import com.edomex.kiliantRSP.service.DavsService.DavsDescAsesoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/davs/asesoria/")
@RequiredArgsConstructor
public class DavsDescAsesoriaController {

    private final DavsDescAsesoriaService davsDescAsesoriaService;

    @GetMapping("/{cveKdm1}/{neyemp}")
    public ResponseEntity<?> obtenerDetalleAsesoria(
            @PathVariable Long cveKdm1,
            @PathVariable String neyemp
    ) {
        try {

            DavsDescAsesoriaDto respuesta =
                    davsDescAsesoriaService.obtenerDescAsesoria(cveKdm1, neyemp);

            return ResponseEntity.ok(respuesta); // ✅ 200

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Parámetros inválidos"); // ❌ 400

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body("No se encontró el detalle de asesoría"); // ❌ 404

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error interno del servidor"); // 💥 500
        }
    }
}
