package com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.AnualisadoPartesPercepciones;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnualisadoDatosPercepciones(

        BigDecimal total,
        String periodo,
        String per1,
        String per2,
        String per3,
        String per4,
        String per5,
        String per6,
        String per7,
        String per8,
        String per9,
        String per10,

        BigDecimal imp1,
        BigDecimal imp2,
        BigDecimal imp3,
        BigDecimal imp4,
        BigDecimal imp5,
        BigDecimal imp6,
        BigDecimal imp7,
        BigDecimal imp8,
        BigDecimal imp9,
        BigDecimal imp10,

        String per1A,
        String per2A,
        String per3A,
        String per4A,
        String per5A,
        String per6A,
        String per7A,
        String per8A,
        String per9A,
        String per10A,
        String per11A,
        String per12A,
        String per13A,
        String per14A,
        String per15A,
        String per16A,
        String per17A,
        String per18A,
        String per19A,
        String per20A,

        BigDecimal imp1A,
        BigDecimal imp2A,
        BigDecimal imp3A,
        BigDecimal imp4A,
        BigDecimal imp5A,
        BigDecimal imp6A,
        BigDecimal imp7A,
        BigDecimal imp8A,
        BigDecimal imp9A,
        BigDecimal imp10A,
        BigDecimal imp11A,
        BigDecimal imp12A,
        BigDecimal imp13A,
        BigDecimal imp14A,
        BigDecimal imp15A,
        BigDecimal imp16A,
        BigDecimal imp17A,
        BigDecimal imp18A,
        BigDecimal imp19A,
        BigDecimal imp20A,

        String per1B,
        String per2B,
        String per3B,
        String per4B,
        String per5B,
        String per6B,
        String per7B,
        String per8B,
        String per9B,
        String per10B,
        String per11B,
        String per12B,
        String per13B,
        String per14B,
        String per15B,
        String per16B,
        String per17B,
        String per18B,
        String per19B,
        String per20B,

        BigDecimal imp1B,
        BigDecimal imp2B,
        BigDecimal imp3B,
        BigDecimal imp4B,
        BigDecimal imp5B,
        BigDecimal imp6B,
        BigDecimal imp7B,
        BigDecimal imp8B,
        BigDecimal imp9B,
        BigDecimal imp10B,
        BigDecimal imp11B,
        BigDecimal imp12B,
        BigDecimal imp13B,
        BigDecimal imp14B,
        BigDecimal imp15B,
        BigDecimal imp16B,
        BigDecimal imp17B,
        BigDecimal imp18B,
        BigDecimal imp19B,
        BigDecimal imp20B

) {

    public String obtenerPer(int index) {
        return switch(index) {
            case 1 -> per1;
            case 2 -> per2;
            case 3 -> per3;
            case 4 -> per4;
            case 5 -> per5;
            case 6 -> per6;
            case 7 -> per7;
            case 8 -> per8;
            case 9 -> per9;
            case 10 -> per10;
            default -> null;
        };
    }

    public BigDecimal obtenerImp(int index) {
        return switch(index) {
            case 1 -> imp1;
            case 2 -> imp2;
            case 3 -> imp3;
            case 4 -> imp4;
            case 5 -> imp5;
            case 6 -> imp6;
            case 7 -> imp7;
            case 8 -> imp8;
            case 9 -> imp9;
            case 10 -> imp10;
            default -> BigDecimal.ZERO;
        };
    }

    public String obtenerPerA(int index) {
        return switch(index) {
            case 1 -> per1A;
            case 2 -> per2A;
            case 3 -> per3A;
            case 4 -> per4A;
            case 5 -> per5A;
            case 6 -> per6A;
            case 7 -> per7A;
            case 8 -> per8A;
            case 9 -> per9A;
            case 10 -> per10A;
            case 11 -> per11A;
            case 12 -> per12A;
            case 13 -> per13A;
            case 14 -> per14A;
            case 15 -> per15A;
            case 16 -> per16A;
            case 17 -> per17A;
            case 18 -> per18A;
            case 19 -> per19A;
            case 20 -> per20A;
            default -> null;
        };
    }

    public BigDecimal obtenerImpA(int index) {
        return switch(index) {
            case 1 -> imp1A;
            case 2 -> imp2A;
            case 3 -> imp3A;
            case 4 -> imp4A;
            case 5 -> imp5A;
            case 6 -> imp6A;
            case 7 -> imp7A;
            case 8 -> imp8A;
            case 9 -> imp9A;
            case 10 -> imp10A;
            case 11 -> imp11A;
            case 12 -> imp12A;
            case 13 -> imp13A;
            case 14 -> imp14A;
            case 15 -> imp15A;
            case 16 -> imp16A;
            case 17 -> imp17A;
            case 18 -> imp18A;
            case 19 -> imp19A;
            case 20 -> imp20A;
            default -> BigDecimal.ZERO;
        };
    }

    public String obtenerPerB(int index) {
        return switch(index) {
            case 1 -> per1B;
            case 2 -> per2B;
            case 3 -> per3B;
            case 4 -> per4B;
            case 5 -> per5B;
            case 6 -> per6B;
            case 7 -> per7B;
            case 8 -> per8B;
            case 9 -> per9B;
            case 10 -> per10B;
            case 11 -> per11B;
            case 12 -> per12B;
            case 13 -> per13B;
            case 14 -> per14B;
            case 15 -> per15B;
            case 16 -> per16B;
            case 17 -> per17B;
            case 18 -> per18B;
            case 19 -> per19B;
            case 20 -> per20B;
            default -> null;
        };
    }

    public BigDecimal obtenerImpB(int index) {
        return switch(index) {
            case 1 -> imp1B;
            case 2 -> imp2B;
            case 3 -> imp3B;
            case 4 -> imp4B;
            case 5 -> imp5B;
            case 6 -> imp6B;
            case 7 -> imp7B;
            case 8 -> imp8B;
            case 9 -> imp9B;
            case 10 -> imp10B;
            case 11 -> imp11B;
            case 12 -> imp12B;
            case 13 -> imp13B;
            case 14 -> imp14B;
            case 15 -> imp15B;
            case 16 -> imp16B;
            case 17 -> imp17B;
            case 18 -> imp18B;
            case 19 -> imp19B;
            case 20 -> imp20B;
            default -> BigDecimal.ZERO;
        };
    }
}

