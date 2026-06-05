package com.edomex.kiliantRSP.util;

public final class KdnomBancoResolver {

    private KdnomBancoResolver() {
    }

    public static String resolverNombreBanco(String bancoCodigo, String numCuenta) {
        if (numCuenta == null || numCuenta.equals("00000000000000")) {
            return "CHEQUE";
        }
        return switch (bancoCodigo) {
            case "13" -> "BANCOMER";
            case "51" -> "BANAMEX";
            case "22" -> "BANORTE";
            case "33" -> "HSBC";
            case "44" -> "SCOTIABANK";
            case "55" -> "SANTANDER";
            case "2" -> "CHEQUE";
            case "0" -> "NO APLICA";
            default -> bancoCodigo;
        };
    }
}
