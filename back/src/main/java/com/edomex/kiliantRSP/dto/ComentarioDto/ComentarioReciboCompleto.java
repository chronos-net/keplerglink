package com.edomex.kiliantRSP.dto.ComentarioDto;

import com.edomex.kiliantRSP.dto.ComentarioDto.*;

import java.util.List;


public record ComentarioReciboCompleto(
        ComentarioCabesera              cabesera,
        List<ComentarioValoresDto>      valores
) {
}
