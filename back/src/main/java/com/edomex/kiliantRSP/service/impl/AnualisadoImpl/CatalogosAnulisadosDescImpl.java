package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.*;
import com.edomex.kiliantRSP.service.AnualisadoService.CatalogosAnulisadosDescService;
import com.edomex.kiliantRSP.service.helpers.Anualisados.CatalogosPercepcionesDescHelper;
import com.edomex.kiliantRSP.service.helpers.Anualisados.CatalogosDeduccionesDescHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CatalogosAnulisadosDescImpl implements  CatalogosAnulisadosDescService {

    private final CatalogosPercepcionesDescHelper catalogosPercepcionesDescHelper;
    private final CatalogosDeduccionesDescHelper catalogosDeduccionesDescHelper;

    @Override
    public CatalogosDto obtenerCatalogosAnualisados(String neyemp, int anio){

        try {
            Map<String, String> percepciones = catalogosPercepcionesDescHelper.obtenerPer(neyemp, anio);

            Map<String, String> deducciones = catalogosDeduccionesDescHelper.obtenerDed(neyemp, anio);

            return new CatalogosDto(
                    new CatalogosPercepcionesDto(percepciones),
                    new CatalogoDeduccionesDto(deducciones)
            );

        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Error obteniendo catalogos",
                    e
            );
        }
    }
}
