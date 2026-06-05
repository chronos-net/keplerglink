package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.service.BuscadorHistoricoDavsService.BuscadorHistoricoDavsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buscadorHistoricoDavs")
@Tag(
        name = "Buscador en vivo del historico Davs",
        description = "Controlador encargado de realizar la búsqueda en vivo del histórico DAVS"
)
public class BuscadorDavsController {

    private final BuscadorHistoricoDavsService buscadorHistoricoDavsService;

    @Operation(
            summary = "Buscador en vivo por clave o nombre",
            description = "Permite buscar registros del histórico DAVS por "
                    + "cliente_o_proveedor (clave) o nombre_cliente. "
                    + "La búsqueda se realiza conforme se escribe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetro inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> buscar(
            @Parameter(
                    description = "Texto a buscar (clave o nombre)",
                    example = "933185641",
                    required = false
            )
            @RequestParam(required = false) String q
    ) {

        if (q == null || q.trim().length() < 2) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(
                buscadorHistoricoDavsService.buscar(q.trim())
        );
    }
}
