package com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioValoresDto;
import java.util.List;

public interface ComentarioReciboExtService {

    List<ComentarioValoresDto> obtenerExt(String neyemp, String periodo, int anio);

}