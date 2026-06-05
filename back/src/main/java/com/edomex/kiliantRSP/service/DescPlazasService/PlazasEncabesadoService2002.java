package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DesglosePlazasEncabesado2002Dto;

public interface PlazasEncabesadoService2002 {
    DesglosePlazasEncabesado2002Dto obtenerEncabezadoPlazas(String neyemp, int anio, String quincena);
}
