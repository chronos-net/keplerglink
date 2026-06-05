package com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkPercepciones;
import com.edomex.kiliantRSP.repository.HistoricoGlinkRepository.HistoricoGlinkPercepcionesRepository;
import com.edomex.kiliantRSP.repository.HistoricoGlinkRepository.HistoricoGlinkPzaempRepository;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkPercepcionesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HistoricoGlinkPercepcionesServiceImpl implements HistoricoGlinkPercepcionesService {

    private final HistoricoGlinkPzaempRepository pzaEmpRepository;
    private final HistoricoGlinkPercepcionesRepository percepcionesRepository;

    @Override
    public HistoricoGlinkPercepciones obtenerPercepciones(String neyemp) {

        // 1. Obtener dependencia + nref
        var datos = pzaEmpRepository.obtenerDepFol(neyemp);

        // 2. Concatenar igual que PHP
        String p1 =(String) datos.get(0)[8];
        String p2 =(String) datos.get(0)[10];

        String plaza = p1 + p2;

        System.out.println(plaza);
        // 3. Obtener total de percepciones
        BigDecimal total = percepcionesRepository.obtenerTotal(plaza);
        if (total == null) total = BigDecimal.ZERO;

        // 4. Devolver DTO
        return new HistoricoGlinkPercepciones(total);
    }
}

