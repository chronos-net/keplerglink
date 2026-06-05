package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioReciboCompleto;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.ComentarioReciboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comentario")
@Tag(name = "Comentarios", description = "Muestra todos los comentarios de un servidor público")
public class ComentarioReciboController {

    private final ComentarioReciboService comentarioService;

    @GetMapping("/consultar")
    @Operation(
            summary = "Muestra los comentarios de los recibos de un servidor público",
            description = "API encargada de mostrar comentarios"
    )
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los comentarios exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ComentarioReciboCompleto obtenerComentario(
            @RequestParam String neyemp,
            @RequestParam String periodo,
            @RequestParam int anio
    ) {
        return comentarioService.obtenerDatosComentarios(neyemp, periodo, anio);
    }
}
