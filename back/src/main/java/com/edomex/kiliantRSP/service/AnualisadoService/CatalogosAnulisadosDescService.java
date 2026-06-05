package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.CatalogosDto;

public interface CatalogosAnulisadosDescService {

    CatalogosDto obtenerCatalogosAnualisados(String neyemp, int anio);
}
