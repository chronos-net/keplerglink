package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkdto;
import com.edomex.kiliantRSP.service.PrestamosGlinkService.PrestamosGlinkService;
import com.edomex.kiliantRSP.service.impl.PrestamosGlinkImpl.PrestamosGlink;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vistaPrestamos")
@Tag(name = "Préstamos GLINK", description = "API que genera los préstamos realizados por el servidor público")
public class PretamosGlinkController {

    private final PrestamosGlinkService prestamosGlinkService;

    @Operation(
            summary = "Consultar préstamos GLINK",
            description = "Permite obtener los préstamos de un servidor público enviando neyemp, periodo y año."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrestamosGlink.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de respuesta exitosa",
                                    value = """
                                            {
                                              "cabeseraPrestamos": {
                                                "neyemp": "820753700",
                                                "negnom": "LEEWORIO GALLARDO MELQUIADES",
                                                "rfc": "LEGM550728RMA",
                                                "fecha_in": "01-02-1977"
                                              },
                                              "descPrestamos": [
                                                {
                                                  "fecha_in": "01-02-1977",
                                                  "cve_ded": "0",
                                                  "puesto": "0",
                                                  "imp_total": 0.0,
                                                  "imp_renta": 0.0,
                                                  "saldo": 0.0,
                                                  "plazos": 0.0,
                                                  "qnas_x_pagar": 0.0,
                                                  "doc_ref": "0"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en los parámetros enviados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Parámetros inválidos\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Ocurrió un error inesperado\"}")
                    )
            )
    })
    @PostMapping("/validacionPrestamos")
    public ResponseEntity<?> creacionPrestamoGlink(@RequestBody PrestamosGlinkdto prestamoDto) {
        try {
            PrestamosGlink respuesta = prestamosGlinkService.creacionPrestamoGlink(prestamoDto);
            return ResponseEntity.ok(respuesta); // ✅ 200

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Parámetros inválidos"); // ❌ 400

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body("No se encontraron préstamos para el servidor público"); // ❌ 404

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error interno del servidor"); // 💥 500
        }
    }
}
