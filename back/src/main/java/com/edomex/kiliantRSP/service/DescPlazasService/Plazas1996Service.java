package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.PlazasDto1996;
import java.util.List;

public interface Plazas1996Service {

    List<PlazasDto1996> obtenerDesglosePlazas(String neyemp, int anio, String quincena);
}
