package com.edomex.kiliantRSP.service.impl.ReciboImpl;

import com.edomex.kiliantRSP.service.recibo.ReciboBuild;
import com.edomex.kiliantRSP.dto.Recibodto.Recibodto;
import com.edomex.kiliantRSP.dto.Recibodto.ReciboRegresoDto;
import com.edomex.kiliantRSP.service.ReciboService.ReciboService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class ReciboServiceImpl implements ReciboService {

    private final ReciboBuild<ReciboRegresoDto> builder1996;
    private final ReciboBuild<ReciboRegresoDto> builder2002;

    public ReciboServiceImpl(
            @Qualifier("builder1996")
            ReciboBuild<ReciboRegresoDto> builder1996,

            @Qualifier("builder2002")
            ReciboBuild<ReciboRegresoDto> builder2002
    ) {
        this.builder1996 = builder1996;
        this.builder2002 = builder2002;
    }

    @Override
    public ReciboRegresoDto getRecibo(Recibodto dto) {

        int anio = dto.anio();

        if (anio >= 1996 && anio <= 2001) {
            return builder1996.build(dto);
        }

        if (anio >= 2002 && anio <= 2022) {
            return builder2002.build(dto);
        }

        throw new RuntimeException("No hay builder definido para el año " + anio);
    }
}