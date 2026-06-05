package com.edomex.kiliantRSP.dto.Recibodto;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.*;
import com.edomex.kiliantRSP.service.impl.ReciboImpl.Recibo;

import java.util.List;

public record ReciboRegresoDto(
        ReciboEmleadoDto            empleado,
        ReciboNumeroDto             recibo,
        ReciboPlazaDto              plaza,
        ResciboResumenDto           resumen,
        List<ReciboConceptoDto>     percepciones,
        List<ReciboConceptoDto>     deducciones,
        List<ReciboMultiPlazasDto>  multiplazas
) implements Recibo{
}
