package com.edomex.kiliantRSP.service.impl.AnioImpl;

import com.edomex.kiliantRSP.dto.AnioDto.Aniodto;
import com.edomex.kiliantRSP.repository.CatAnioRepository;
import com.edomex.kiliantRSP.service.AnioService.AnioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnioServiceImpl implements AnioService {

    private final CatAnioRepository catAnioRepository;

    @Override
    public List<Aniodto> obtenerDatosAnios() {
        return catAnioRepository.findAll()
                .stream()
                .map(a -> new Aniodto(a.getCveAnio(), a.getDescAnio()))
                .collect(Collectors.toList());
    }
}
