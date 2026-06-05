package com.edomex.kiliantRSP.service.impl.ReciboImpl.ComentarioXRecibo;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioValoresDto;
import com.edomex.kiliantRSP.service.ReciboService.ComentariosxRecibo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioReciboValoresImpl implements ComentarioReciboValoresService {

    private final ComentarioReciboDevService devService;
    private final ComentarioReciboCanService canService;
    private final ComentarioReciboExtService extService;
    private final ComentarioCompService compService;


    @Override
    public List<ComentarioValoresDto> obtenerValoresService(String neyemp, String periodo, int anio){

        List<ComentarioValoresDto> resultado = new ArrayList<>();

        resultado.addAll(devService.obtenerDev(neyemp, periodo, anio));
        resultado.addAll(canService.obtenerCan(neyemp, periodo, anio));
        resultado.addAll(extService.obtenerExt(neyemp, periodo, anio));
        resultado.addAll(compService.obtenerComp(neyemp, periodo, anio));

        return resultado.stream()
                .sorted(
                        Comparator.comparing(ComentarioValoresDto::anio, Comparator.reverseOrder())
                                .thenComparing(ComentarioValoresDto::qna, Comparator.reverseOrder())
                )
                .toList();
    }
}