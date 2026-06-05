package com.edomex.kiliantRSP.dto.DavsDto;

import java.math.BigDecimal;
import java.util.Date;

public record DavsSolicitudApoyoDto(
        Long cveKdm1,
        String folioDocumento,
        String tipoMovimiento,
        Date fechaPago,
        Integer plazoDias,
        String clienteProveedor,
        Date fecha,
        BigDecimal saldoDocumento,
        String nombre,
        String rfc
) {}