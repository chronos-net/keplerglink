package com.edomex.kiliantRSP.service.impl.PrestamosGlinkImpl;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkDesc;
import com.edomex.kiliantRSP.service.PrestamosGlinkService.PrestamosGlinkDescService;
import com.edomex.kiliantRSP.repository.PrestamosGlinkRepository.PrestamosGlinkDescRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamosGlinkDescServiceImpl implements PrestamosGlinkDescService {

    private final PrestamosGlinkDescRepository prestamosGlinkDescRepository;

    @Override
    public List<PrestamosGlinkDesc> obtenerDescripciones(String neyemp) {
        return prestamosGlinkDescRepository.buscarDesc(neyemp);
    }
}
