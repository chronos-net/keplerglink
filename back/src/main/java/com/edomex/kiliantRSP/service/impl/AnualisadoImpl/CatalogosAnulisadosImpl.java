package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.*;
import com.edomex.kiliantRSP.service.AnualisadoService.CatalogosAnulisadosService;
import com.edomex.kiliantRSP.service.helpers.Anualisados.CatalogosPercepcionesHelper;
import com.edomex.kiliantRSP.service.helpers.Anualisados.CatalogosDeduccionesHelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CatalogosAnulisadosImpl implements CatalogosAnulisadosService {

    private final CatalogosPercepcionesHelper   catalogosPercepcionesHelper;
    private final CatalogosDeduccionesHelper    catalogosDeduccionesHelper;


    @Override
    public CatalogosDto obtenerCatalogosAnualisado(String neyemp, int anio){

        try {
            Map<String, String> percepciones = catalogosPercepcionesHelper.obtenerPer(neyemp, anio);

            Map<String, String> deducciones = catalogosDeduccionesHelper.obtenerDed(neyemp, anio);

            return new CatalogosDto(
                    new CatalogosPercepcionesDto(percepciones),
                    new CatalogoDeduccionesDto(deducciones)
            );

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Error obteniendo catalogos",
                    e
            );
        }

    }

}
