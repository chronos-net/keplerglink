package com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes;

public record ReciboNumeroDto(
        String numRecibo,
        String bancoFinal,
        String banco,
        String numCuenta,
        String lugarPago
) {
}
