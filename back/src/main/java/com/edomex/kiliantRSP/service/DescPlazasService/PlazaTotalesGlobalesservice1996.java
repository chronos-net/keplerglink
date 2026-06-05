package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.TotalGlobales1996Dto;

public interface PlazaTotalesGlobalesservice1996 {

    TotalGlobales1996Dto obtenerTotalGlobal(String neyemp, int anio, String quincena);
}
