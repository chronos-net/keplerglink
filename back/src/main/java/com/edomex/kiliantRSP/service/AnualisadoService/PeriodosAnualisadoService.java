package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.PeriodoDto;

import java.util.List;


public interface PeriodosAnualisadoService {

    List<PeriodoDto> obtenerPeridosAnualisado(String neyemp, int anio);
}
