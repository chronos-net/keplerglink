package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.Recibodto;
import com.edomex.kiliantRSP.dto.Recibodto.ReciboRegresoDto;

public interface ReciboService {
    ReciboRegresoDto getRecibo(Recibodto dto);
}