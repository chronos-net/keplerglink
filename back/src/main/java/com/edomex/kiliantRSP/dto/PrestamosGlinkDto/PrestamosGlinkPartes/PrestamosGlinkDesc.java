package com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes;

import java.math.BigDecimal;

public record PrestamosGlinkDesc(
        String fecha_in,
        String cve_ded,
        String puesto,
        BigDecimal imp_total,
        BigDecimal imp_renta,
        BigDecimal saldo,
        BigDecimal plazos,
        BigDecimal qnas_x_pagar,
        String doc_ref
) {
}
