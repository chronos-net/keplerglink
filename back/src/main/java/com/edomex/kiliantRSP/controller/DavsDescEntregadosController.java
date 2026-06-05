package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.DavsDto.DavsDescEntregadoDto;
import com.edomex.kiliantRSP.service.DavsService.DavsDescEntregadosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/davs/entregado")
@RequiredArgsConstructor
public class DavsDescEntregadosController {

    private final DavsDescEntregadosService davsDescEntregadosService;

    @GetMapping("/{cveKdm1}")
    public ResponseEntity<?> obtenerDetalleEntregado(@PathVariable Long cveKdm1){
        try {
            DavsDescEntregadoDto respuesta =  davsDescEntregadosService.obtenerDescEntregado(cveKdm1);
            return ResponseEntity.ok(respuesta);
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
