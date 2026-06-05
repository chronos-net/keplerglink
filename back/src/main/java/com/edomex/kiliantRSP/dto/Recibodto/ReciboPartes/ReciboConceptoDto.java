package com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes;


import java.math.BigDecimal;

public record ReciboConceptoDto(
        String codigo,
        String descripcion,
        BigDecimal importe
) {
}
