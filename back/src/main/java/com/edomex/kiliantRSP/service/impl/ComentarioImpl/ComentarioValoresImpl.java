package com.edomex.kiliantRSP.service.impl.ComentarioImpl;

import com.edomex.kiliantRSP.dto.ComentarioDto.ComentarioValoresDto;
import com.edomex.kiliantRSP.service.ComentarioService.*;
import com.edomex.kiliantRSP.service.ComentarioService.ComentarioValoresService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioValoresImpl implements  ComentarioValoresService{

    private final ComentarioDevService      devService;
    private final ComentarioCanService      canService;
    private final ComentarioExtService      extService;
    private final ComentariosCompService    comentariosCompService;

    @Override
    public List<ComentarioValoresDto> obtenerValoresService(String neyemp) {
         List<ComentarioValoresDto> resultado = new ArrayList<>();
         resultado.addAll(devService.obtenerDev(neyemp));
         resultado.addAll(extService.obtenerExt(neyemp));
         resultado.addAll(canService.obtenerCan(neyemp));
         resultado.addAll(comentariosCompService.obtenerComp(neyemp));




        System.out.println("DEV: " + devService.obtenerDev(neyemp).size());
        System.out.println("CAN: " + canService.obtenerCan(neyemp).size());
        System.out.println("EXT: " + extService.obtenerExt(neyemp).size());
        System.out.println("TOTAL: " + resultado.size());
        return resultado.stream()
                .sorted(
                        Comparator.comparing(ComentarioValoresDto::anio)
                                .thenComparing(ComentarioValoresDto::qna)
                                .reversed()
                )
                .toList();
    }
}
