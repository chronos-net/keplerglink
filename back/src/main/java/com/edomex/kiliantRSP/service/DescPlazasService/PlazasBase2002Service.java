package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.PlazaBaseDto1996;

import java.util.List;

public interface PlazasBase2002Service {

    List<PlazaBaseDto1996> obtenerPlazasBase(String neyemp, Integer anio, String quincena);
}
