package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.CatalogosDto;

public interface CatalogosAnulisadosService {

    CatalogosDto obtenerCatalogosAnualisado(String neyemp, int anio);
}
