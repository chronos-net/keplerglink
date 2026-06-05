package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.PensionesGlinkDto.PensionesGlinkDto;
import com.edomex.kiliantRSP.dto.PensionesGlinkDto.PensionesGlinkResponse;
import com.edomex.kiliantRSP.service.PensionesGlinkService.PensionesGlinkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pensionesGLINK")
@Tag(
        name = "Pensiones GLINK",
        description = "Controlador encargado de consultar las pensiones asociadas a un Servidor Público"
)
public class PensionesGlinkController {

    private final PensionesGlinkService pensionesService;

    @PostMapping("/consultar")
    @Operation(
            summary = "Consulta pensiones por clavesp",
            description = "Devuelve una lista con todas las pensiones encontradas para la clave SP proporcionada.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de solicitud para consultar pensiones",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de solicitud",
                                            value = """
                                                    {
                                                        "neyemp": "995068883",
                                                        "negnom": "CERVANTES CRUZ LUCERITO",
                                                        "rfc": "CECL881127MU9"
                                                    }                                 
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consulta exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PensionesGlinkResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = """
                                                            [
                                                                {
                                                                    "clavesp": "995068883",
                                                                    "nombresp": "HERRERA VAZQUEZ JULIO CESAR             ",
                                                                    "rfc": "HEVJ7679C77",
                                                                    "fechain": "1-4-1999",
                                                                    "nombrepension": "MEDINA SALGUERO MARGARITA               ",
                                                                    "tipo_desc": "S-INT",
                                                                    "altaqna": 12,
                                                                    "altano": 2011,
                                                                    "porcentaje": 40.0,
                                                                    "importe": 2747.84,
                                                                    "referencia": "34912"
                                                                }
                                                            ]
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud incorrecta"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor"
                    )
            }
    )
    public ResponseEntity<?> consultarPensiones(@RequestBody PensionesGlinkDto dto) {
        try {

            List<PensionesGlinkResponse> respuesta = pensionesService.consultarPensiones(dto);

            if (respuesta == null || respuesta.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body("No se encontraron pensiones para el servidor público");
            }

            return ResponseEntity.ok(respuesta); // ✅ 200

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Solicitud incorrecta"); // ❌ 400

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error interno del servidor"); // 💥 500
        }
    }
}
