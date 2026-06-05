package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoPartesDeducciones;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnualisadoDatosDeducciones(

        BigDecimal total,
        String periodo,

        // Grupo normal (1–10)
        String ded1, String ded2, String ded3, String ded4, String ded5,
        String ded6, String ded7, String ded8, String ded9, String ded10,

        BigDecimal imp1, BigDecimal imp2, BigDecimal imp3, BigDecimal imp4, BigDecimal imp5,
        BigDecimal imp6, BigDecimal imp7, BigDecimal imp8, BigDecimal imp9, BigDecimal imp10,

        // Grupo A (1–20)
        String ded1A, String ded2A, String ded3A, String ded4A, String ded5A,
        String ded6A, String ded7A, String ded8A, String ded9A, String ded10A,
        String ded11A, String ded12A, String ded13A, String ded14A, String ded15A,
        String ded16A, String ded17A, String ded18A, String ded19A, String ded20A,

        BigDecimal imp1A, BigDecimal imp2A, BigDecimal imp3A, BigDecimal imp4A, BigDecimal imp5A,
        BigDecimal imp6A, BigDecimal imp7A, BigDecimal imp8A, BigDecimal imp9A, BigDecimal imp10A,
        BigDecimal imp11A, BigDecimal imp12A, BigDecimal imp13A, BigDecimal imp14A, BigDecimal imp15A,
        BigDecimal imp16A, BigDecimal imp17A, BigDecimal imp18A, BigDecimal imp19A, BigDecimal imp20A,

        // Grupo B (1–20)
        String ded1B, String ded2B, String ded3B, String ded4B, String ded5B,
        String ded6B, String ded7B, String ded8B, String ded9B, String ded10B,
        String ded11B, String ded12B, String ded13B, String ded14B, String ded15B,
        String ded16B, String ded17B, String ded18B, String ded19B, String ded20B,

        BigDecimal imp1B, BigDecimal imp2B, BigDecimal imp3B, BigDecimal imp4B, BigDecimal imp5B,
        BigDecimal imp6B, BigDecimal imp7B, BigDecimal imp8B, BigDecimal imp9B, BigDecimal imp10B,
        BigDecimal imp11B, BigDecimal imp12B, BigDecimal imp13B, BigDecimal imp14B, BigDecimal imp15B,
        BigDecimal imp16B, BigDecimal imp17B, BigDecimal imp18B, BigDecimal imp19B, BigDecimal imp20B

) {

    // ===================== MÉTODOS DE ACCESO =====================

    public String obtenerDed(int index) {
        return switch (index) {
            case 1 -> ded1;  case 2 -> ded2;  case 3 -> ded3;  case 4 -> ded4;  case 5 -> ded5;
            case 6 -> ded6;  case 7 -> ded7;  case 8 -> ded8;  case 9 -> ded9;  case 10 -> ded10;
            default -> null;
        };
    }

    public BigDecimal obtenerImp(int index) {
        return switch (index) {
            case 1 -> imp1;  case 2 -> imp2;  case 3 -> imp3;  case 4 -> imp4;  case 5 -> imp5;
            case 6 -> imp6;  case 7 -> imp7;  case 8 -> imp8;  case 9 -> imp9;  case 10 -> imp10;
            default -> BigDecimal.ZERO;
        };
    }

    public String obtenerDedA(int index) {
        return switch (index) {
            case 1 -> ded1A;  case 2 -> ded2A;  case 3 -> ded3A;  case 4 -> ded4A;  case 5 -> ded5A;
            case 6 -> ded6A;  case 7 -> ded7A;  case 8 -> ded8A;  case 9 -> ded9A;  case 10 -> ded10A;
            case 11 -> ded11A; case 12 -> ded12A; case 13 -> ded13A; case 14 -> ded14A; case 15 -> ded15A;
            case 16 -> ded16A; case 17 -> ded17A; case 18 -> ded18A; case 19 -> ded19A; case 20 -> ded20A;
            default -> null;
        };
    }

    public BigDecimal obtenerImpA(int index) {
        return switch (index) {
            case 1 -> imp1A;  case 2 -> imp2A;  case 3 -> imp3A;  case 4 -> imp4A;  case 5 -> imp5A;
            case 6 -> imp6A;  case 7 -> imp7A;  case 8 -> imp8A;  case 9 -> imp9A;  case 10 -> imp10A;
            case 11 -> imp11A; case 12 -> imp12A; case 13 -> imp13A; case 14 -> imp14A; case 15 -> imp15A;
            case 16 -> imp16A; case 17 -> imp17A; case 18 -> imp18A; case 19 -> imp19A; case 20 -> imp20A;
            default -> BigDecimal.ZERO;
        };
    }

    public String obtenerDedB(int index) {
        return switch (index) {
            case 1 -> ded1B;  case 2 -> ded2B;  case 3 -> ded3B;  case 4 -> ded4B;  case 5 -> ded5B;
            case 6 -> ded6B;  case 7 -> ded7B;  case 8 -> ded8B;  case 9 -> ded9B;  case 10 -> ded10B;
            case 11 -> ded11B; case 12 -> ded12B; case 13 -> ded13B; case 14 -> ded14B; case 15 -> ded15B;
            case 16 -> ded16B; case 17 -> ded17B; case 18 -> ded18B; case 19 -> ded19B; case 20 -> ded20B;
            default -> null;
        };
    }

    public BigDecimal obtenerImpB(int index) {
        return switch (index) {
            case 1 -> imp1B;  case 2 -> imp2B;  case 3 -> imp3B;  case 4 -> imp4B;  case 5 -> imp5B;
            case 6 -> imp6B;  case 7 -> imp7B;  case 8 -> imp8B;  case 9 -> imp9B;  case 10 -> imp10B;
            case 11 -> imp11B; case 12 -> imp12B; case 13 -> imp13B; case 14 -> imp14B; case 15 -> imp15B;
            case 16 -> imp16B; case 17 -> imp17B; case 18 -> imp18B; case 19 -> imp19B; case 20 -> imp20B;
            default -> BigDecimal.ZERO;
        };
    }
}
