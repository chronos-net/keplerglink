package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.service.BuscadorPrestamamosGlinkService.BuscadorPrestamosGlinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buscadorPrestamosGlink")
@Tag(
        name = "Buscador Prestamos GLINK",
        description = "Búsqueda en vivo de Servidores Públicos que cuentan con préstamo activo"
)
public class BuscadorPrestamosController {

    private final BuscadorPrestamosGlinkService buscadorPrestamosGlinkService;

    @GetMapping
    @Operation(
            summary = "Búsqueda en vivo de Servidores Públicos",
            description = "Busca coincidencias por clave SP (neyemp) en tiempo real. Devuelve una lista de resultados parciales.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta realizada correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Object.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = """
                                                    [
                                                      {
                                                        "neyemp": "997552205",
                                                        "negnom": "DIAZ ZAMORA CLAUDIA ARELI",
                                                        "rfc": "DIZC820823LV2"
                                                      },
                                                      {
                                                        "neyemp": "997552214",
                                                        "negnom": "DIAZLEAL VALDEZ JUAN",
                                                        "rfc": "DIVJ860714MF4"
                                                      },
                                                      {
                                                        "neyemp": "997552223",
                                                        "negnom": "DOMIGUEZ PEREZ AARON",
                                                        "rfc": "DOPA830831000"
                                                      }
                                                    ]
                                                    """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<?> buscadorPorNeyemp(@RequestParam("q") String clavesp) {

        var resultado = buscadorPrestamosGlinkService.buscadorPorNeyemp(clavesp);
        return ResponseEntity.ok(resultado);
    }
}
