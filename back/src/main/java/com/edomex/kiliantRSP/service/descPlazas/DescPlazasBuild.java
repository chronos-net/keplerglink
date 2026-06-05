package com.edomex.kiliantRSP.service.descPlazas;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DescPlazasdto;

public interface DescPlazasBuild<T> {

    /**
     * Interfaz genérica tipo Builder encargada de construir un DTO de salida
     * específico a partir de un DTO de entrada (DescPlazasdto).
     * Permite separar la lógica de construcción según reglas de negocio o periodos históricos.
     */

    T build(DescPlazasdto raw);
}
