package com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes;

import java.math.BigDecimal;

public record ReciboMultiPlazasDto(
        String plaza,
        String puesto,
        String leyendaPuesto,
        String adsc,
        BigDecimal neto
) {
}
