package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentariosDto;
import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioReciboCompleto;
import com.edomex.kiliantRSP.service.ComentarioService.ComentariosService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comentarios")
@Tag(name = "Comentarios", description = "Muestra todos los comentarios de un servidor público")
public class ComentariosController {

    private final ComentariosService comentariosService;

    @PostMapping("/ServidorPublicoComentarios")
    @Operation(
            summary = "Muestra los comentarios de los recibos de un servidor público",
            description = "API encargada de mostrar comentarios"
    )
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los comentarios exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<?> obtenerLosComentarios(@RequestBody ComentariosDto request){
        try {
            ComentarioReciboCompleto response = comentariosService.obtenerComentarios(request);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
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