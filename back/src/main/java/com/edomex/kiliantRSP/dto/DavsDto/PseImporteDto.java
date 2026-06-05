package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;

public record PseImporteDto(
        String numTipoDocumento,
        String folioDocumentoAnexar,
        String destinatarioCheque,
        String tipoMovimiento,
        BigDecimal importe,
        BigDecimal importe2
) {}
