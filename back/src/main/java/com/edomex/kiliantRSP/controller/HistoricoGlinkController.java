package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkdto;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkService;
import com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl.HistoricoGlink;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vistaHistorico")
@Tag(name = "Vista Historico", description = "Controlador de mostrar el cuerpo del historico glink")
public class  HistoricoGlinkController {

    private final HistoricoGlinkService vistaHistoricoService;

    @PostMapping("/validacionHistorico")
    @Operation(
            summary = "GENERA EL HISTORICO DEL SP",
            description = "Recibe los parametros del servidor publico y devuele el historico ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "parametros necesarios para generar el historico",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de solicitud",
                                            value = """
                                                    {
                                                        "neyemp": "821533011",
                                                        "negnom": "CERVANTES CRUZ LUCERITO",
                                                        "rfc": "CECL881127MU9"
                                                    }
                                                    """
                                    )
                            }
                    )

            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Historico de glink",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.edomex.kiliantRSP.dto.Recibodto.ReciboRegresoDto.class),
                                    examples ={
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = """
                                                            {
                                                                "cabeseraHistorico": {
                                                                    "neyemp": "821533011",
                                                                    "negnom": "ESPINOZA RAMIREZ ROSA EVELIA            ",
                                                                    "rfc": "EIRR58313LUA",
                                                                    "sitsp": "0 ACTIVO",
                                                                    "curp": "EIRR580313MMCSMS08  ",
                                                                    "dep": "2051",
                                                                    "uresp": "1012",
                                                                    "npza": "6442 ",
                                                                    "cct": "15EPR2836S",
                                                                    "nh": 0,
                                                                    "catego": "A307170",
                                                                    "perdeocupacion": "1632011-99999999",
                                                                    "desc_puesto": "SUBD ESC PRE PR"
                                                                },
                                                                "percepcionesHistorico": {
                                                                    "total": 17432.1
                                                                },
                                                                "descHistorico": [
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2050",
                                                                        "uresponsable": "23102",
                                                                        "plaza": "00000",
                                                                        "cct": null,
                                                                        "nhoras": "1",
                                                                        "thoras": "1",
                                                                        "catego": "A0362413",
                                                                        "perocupacion": "199217-199307",
                                                                        "totalpercep": "1357",
                                                                        "motivobaja": "1",
                                                                        "tipodeplaza": "1",
                                                                        "idmb": "1",
                                                                        "descripcion": "ALTA"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2050",
                                                                        "uresponsable": "23103",
                                                                        "plaza": "00000",
                                                                        "cct": null,
                                                                        "nhoras": "1",
                                                                        "thoras": "0",
                                                                        "catego": "A0381410",
                                                                        "perocupacion": "199307-199501",
                                                                        "totalpercep": "947",
                                                                        "motivobaja": "1",
                                                                        "tipodeplaza": "1",
                                                                        "idmb": "1",
                                                                        "descripcion": "ALTA"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2050",
                                                                        "uresponsable": "23201",
                                                                        "plaza": "81005",
                                                                        "cct": "15EES0772D",
                                                                        "nhoras": "0",
                                                                        "thoras": "0",
                                                                        "catego": "A0380517",
                                                                        "perocupacion": "199620-199716",
                                                                        "totalpercep": "3640",
                                                                        "motivobaja": "9",
                                                                        "tipodeplaza": "4",
                                                                        "idmb": "9",
                                                                        "descripcion": null
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2050",
                                                                        "uresponsable": "23103",
                                                                        "plaza": "20860",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "2",
                                                                        "thoras": "1",
                                                                        "catego": "A0380913",
                                                                        "perocupacion": "199501-199806",
                                                                        "totalpercep": "2835",
                                                                        "motivobaja": "1",
                                                                        "tipodeplaza": "4",
                                                                        "idmb": "1",
                                                                        "descripcion": "ALTA"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "06442",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "14",
                                                                        "thoras": "0",
                                                                        "catego": "A0307170",
                                                                        "perocupacion": "201801-162018",
                                                                        "totalpercep": "81600",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "06442",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "14",
                                                                        "thoras": "0",
                                                                        "catego": "A0307170",
                                                                        "perocupacion": "201801-162018",
                                                                        "totalpercep": "81600",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10201",
                                                                        "plaza": "81005",
                                                                        "cct": "15EES0965S",
                                                                        "nhoras": "15",
                                                                        "thoras": "1",
                                                                        "catego": "A0510220",
                                                                        "perocupacion": "201801-252018",
                                                                        "totalpercep": "12500",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10201",
                                                                        "plaza": "81005",
                                                                        "cct": "15EES0965S",
                                                                        "nhoras": "15",
                                                                        "thoras": "1",
                                                                        "catego": "A0510220",
                                                                        "perocupacion": "201801-252018",
                                                                        "totalpercep": "12500",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "12769",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "0",
                                                                        "thoras": "0",
                                                                        "catego": "A0306360",
                                                                        "perocupacion": "201802-201815",
                                                                        "totalpercep": "16184",
                                                                        "motivobaja": "9",
                                                                        "tipodeplaza": "4",
                                                                        "idmb": "9",
                                                                        "descripcion": null
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "20860",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "0",
                                                                        "thoras": "0",
                                                                        "catego": "A0111130",
                                                                        "perocupacion": "199807-201105",
                                                                        "totalpercep": "9776",
                                                                        "motivobaja": "H",
                                                                        "tipodeplaza": "4",
                                                                        "idmb": null,
                                                                        "descripcion": null
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10201",
                                                                        "plaza": "81005",
                                                                        "cct": "15EES0772D",
                                                                        "nhoras": "14",
                                                                        "thoras": "0",
                                                                        "catego": "A0510220",
                                                                        "perocupacion": "200511-012006",
                                                                        "totalpercep": "103100",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10201",
                                                                        "plaza": "81005",
                                                                        "cct": "15EES0772D",
                                                                        "nhoras": "14",
                                                                        "thoras": "0",
                                                                        "catego": "A0510220",
                                                                        "perocupacion": "200511-012006",
                                                                        "totalpercep": "103100",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "20860",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "14",
                                                                        "thoras": "0",
                                                                        "catego": "A0111130",
                                                                        "perocupacion": "200511-012006",
                                                                        "totalpercep": "103100",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2051",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "20860",
                                                                        "cct": "15EPR2836S",
                                                                        "nhoras": "14",
                                                                        "thoras": "0",
                                                                        "catego": "A0111130",
                                                                        "perocupacion": "200511-012006",
                                                                        "totalpercep": "103100",
                                                                        "motivobaja": "Z",
                                                                        "tipodeplaza": "0",
                                                                        "idmb": "Z",
                                                                        "descripcion": "LICENCIA SIN GOCE"
                                                                    },
                                                                    {
                                                                        "negnom": "821533011",
                                                                        "dependencia": "2057",
                                                                        "uresponsable": "10102",
                                                                        "plaza": "20275",
                                                                        "cct": "15EPR0725Z",
                                                                        "nhoras": "0",
                                                                        "thoras": "0",
                                                                        "catego": "A0304480",
                                                                        "perocupacion": "200521-200620",
                                                                        "totalpercep": "16195",
                                                                        "motivobaja": "9",
                                                                        "tipodeplaza": "4",
                                                                        "idmb": "9",
                                                                        "descripcion": null
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    }
                            )        )
            }
    )
    public ResponseEntity<?> creacionHistoricoGlink(@RequestBody HistoricoGlinkdto VistaCHdto){
        try {

            HistoricoGlink historicoGlink = vistaHistoricoService.getVistaHistorico(VistaCHdto);

            return ResponseEntity.ok(historicoGlink);
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
