package com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeduccionesAnti;

import java.math.BigDecimal;
import java.util.List;

public record DeduccionesPlazaDto(
        String secuenciaPlaza,
        BigDecimal total,
        List<DescPlazasDatosDeduccionesAnti> deducciones
) {
}
