package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.Recibodto.Recibodto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboService;
import com.edomex.kiliantRSP.service.impl.ReciboImpl.Recibo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recibos")
@Tag(name = "Recibo", description = "Controlador encargado de crear el resibo")
public class ReciboController {

    private final ReciboService reciboService;

    //funcion principal, encargada de  generar el recibo
    @PostMapping("/validacionRecibo")
    @Operation(
            summary = "Genera el recibo de nómina",
            description = "Recibe los parámetros del servidor público y devuelve el recibo completo con percepciones y deducciones.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Parámetros necesarios para generar el recibo",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de solicitud",
                                            value = """
                    {
                      "quincena": "02",
                      "anio": 2002,
                      "nombreSp": "Vladimir Lemus",
                      "neyemp": "821691861"
                    }
                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Recibo generado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.edomex.kiliantRSP.dto.Recibodto.ReciboRegresoDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = """
                        {
                          "datosPersonales": {
                            "nombre": "GAMEZ MONTERO LETICIA",
                            "curp": "GAML631228MVZMNT08",
                            "rfc": "GAML631228KZ5",
                            "iss": "169186"
                          },
                          "datosPuestosDependencias": [
                            {
                              "lugar_pago": "15ECT0053N",
                              "plaza": "205293336",
                              "adsc": "205210200",
                              "puesto": "A0610381",
                              "leyenda_puesto": "ORIENT TEC A MS",
                              "descripcion": "SUBDIR DE BACHILLER TECN",
                              "total": null
                            }
                          ],
                          "datosCantidades": {
                            "lugPago": "",
                            "numCuenta": "00000000000000",
                            "totalPercep": 8254.34,
                            "totalDed": 1198.04,
                            "banco": "1",
                            "numRecibo": "1",
                            "neto": 7056.30
                          },
                          "percepcionesdesc": [
                            {
                              "total": 4524.84,
                              "percepciones": [
                                {"per": "0102", "descPer": "SUELDO BASE", "imp": 2973.40},
                                {"per": "0322", "descPer": "LABORES DOCENTES", "imp": 297.35}
                              ]
                            }
                          ],
                          "datosDeduccionesDesc": [
                            {
                              "total": 61.12,
                              "deducciones": [
                                {"ded": "5462", "descDed": "CUOTA SINDICATO MAGISTERI", "imp": 16.52},
                                {"ded": "5499", "descDed": "SEGURIDAD SOCIAL SMSEM", "imp": 44.60}
                              ]
                            }
                          ]
                        }
                        """
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<?> creacionReciboKiliant(@RequestBody Recibodto kdrhempdto) {
        try {

            Recibo recibo = reciboService.getRecibo(kdrhempdto);

            return ResponseEntity.ok(recibo); // 200

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
