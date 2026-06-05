package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.PercepcionesPlazaDto;
import java.util.List;

public interface Percepciones1996Service {

    List<PercepcionesPlazaDto> obtenerPercepciones(
            String neyemp,
            int anio,
            String quincena
    );
}
