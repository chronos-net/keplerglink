package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DescPlazasdto;
import com.edomex.kiliantRSP.service.DescPlazasService.DescPlazasService;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.DescPlazas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/descPlazas")
@Tag(
        name = "Desgloses de plazas",
        description = "Controlador encargado de generar el desglose de plazas por servidor público"
)
public class DesPlazasController {
    /**
     * Controlador REST encargado de exponer los endpoints del módulo DescPlazas.
     * Recibe las peticiones del cliente, valida/parcea los parámetros de entrada
     * y delega la lógica de negocio al DescPlazasService.
     */

    private final DescPlazasService descPlazasService;

    @PostMapping("/validacionDescPlazas")
    @Operation(
            summary = "Genera el desglose de plazas",
            description = "Recibe los datos del servidor público y devuelve el desglose completo de plazas, percepciones y deducciones.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Parámetros necesarios para generar el desglose de plazas",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de solicitud",
                                            value = """
                        {
                          "quincena": "01",
                          "anio": 2014,
                          "nombreSp": "Vladimir Lemus",
                          "neyemp": "997283288"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Desglose de plazas generado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DescPlazas.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = """
                            {
                              "descPlazasDatosPlazaPrincipalDesc": {
                                "neyemp": "997283288",
                                "negnom": "BASILIO FIGUEROA AARON",
                                "rfc": "BAFA781121QS4",
                                "cheque": "5495107",
                                "plaza": "205966019",
                                "puesto": "A0120941",
                                "leyenda_puesto": "HORA CLASE A ME",
                                "ads": "205110201",
                                "lugpago": "15EST0766L",
                                "centro_trabajo": "0570571MB1",
                                "percep": 14498.47,
                                "ded": 4315.50,
                                "totalRecibido": 10182.97,
                                "secuencia_plaza": "1"
                              },
                              "descPlazasDatosPercepciones": [],
                              "descPlazasDatosDeducciones": [],
                              "descPlazasDatosporPlazaDescs": []
                            }
                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "No se encontraron plazas"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<?> creacionDescPlazas(@RequestBody DescPlazasdto kdrhempdto){

        try {
            DescPlazas plazas = descPlazasService.getDescPlazas(kdrhempdto);

            return ResponseEntity.ok(plazas);
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
