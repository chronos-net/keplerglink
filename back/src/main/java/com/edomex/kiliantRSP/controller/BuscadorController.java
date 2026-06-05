package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.service.BuscadoService.BuscadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buscadorKiliant")
@Tag(
        name = "Buscador En Vivo Kiliant",
        description = "Busca en vivo coincidencias por nombre, devolviendo los servidores públicos relacionados"
)
public class BuscadorController {

    private final BuscadorService buscadorService;

    /// metodo principal
    @GetMapping
    @Operation(
            summary = "Buscar servidores públicos por nombre",
            description = "Realiza una búsqueda en vivo devolviendo coincidencias basadas en fragmentos del nombre.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resultados encontrados correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Object.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = """
                                                    [
                                                      { "negnom": "DURAN GONZALEZ JUAN FERNANDO", "neyemp": "00000012" },
                                                      { "negnom": "BASTIDA ALVAREZ JUANA", "neyemp": "00000027" },
                                                      { "negnom": "BERMUDEZ LUCIO JUAN LUIS", "neyemp": "00000033" },
                                                      { "negnom": "GONZALEZ MALVAEZ JUAN", "neyemp": "00000090" },
                                                      { "negnom": "HERNANDEZ CARRENO JUAN GALDINO", "neyemp": "00000097" },
                                                      { "negnom": "ZETINA GARCIA JUAN", "neyemp": "00000235" },
                                                      { "negnom": "CRUZ AGUILAR JUAN JOSE", "neyemp": "00000251" },
                                                      { "negnom": "ZARZA CHIAPA JUAN CARLOS", "neyemp": "00000346" },
                                                      { "negnom": "HERNANDEZ COTA JUAN GERARDO", "neyemp": "00000392" },
                                                      { "negnom": "MACIAS VELAZQUEZ JUAN MANUEL", "neyemp": "00000629" }
                                                    ]
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parámetros inválidos"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> buscadorPorNombre(@RequestParam("q") String nombre) {

        var resultado = buscadorService.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultado);
    }
}
