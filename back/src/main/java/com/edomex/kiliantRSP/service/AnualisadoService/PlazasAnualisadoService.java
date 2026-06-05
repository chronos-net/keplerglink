package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.PlazasAnualisadosDto;

import java.util.List;


public interface PlazasAnualisadoService {
    List<PlazasAnualisadosDto> obtenerPlazasAnalisados(String neyemp, int anio);
}
