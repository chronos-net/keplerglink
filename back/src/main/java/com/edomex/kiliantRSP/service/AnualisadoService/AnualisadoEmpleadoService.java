package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoEmpleadoDto;

public interface AnualisadoEmpleadoService {

    AnualisadoEmpleadoDto obtenerEmpleado(String neyemp, int anio);
}
