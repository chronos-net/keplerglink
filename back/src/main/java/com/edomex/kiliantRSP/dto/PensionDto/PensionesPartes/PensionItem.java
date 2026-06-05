package com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes;

public record PensionItem(
        String negnom,
        String bco,
        String lugar_pago,
        String cantidad,
        String cheque,
        String n_cuenta
) { }