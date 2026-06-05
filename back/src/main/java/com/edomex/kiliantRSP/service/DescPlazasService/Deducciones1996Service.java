package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DeduccionesPlazaDto;
import java.util.List;

public interface Deducciones1996Service {

    List<DeduccionesPlazaDto> obtenerDeducciones(
            String neyemp,
            int anio,
            String quincena
    );

}
