package com.edomex.kiliantRSP.service.impl.PeriodoImpl;

import com.edomex.kiliantRSP.dto.PeriodoDto.Periododto;
import com.edomex.kiliantRSP.repository.CatPeriodoRepository;
import com.edomex.kiliantRSP.service.PeriodoService.PeriodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeriodoServiceImpl implements PeriodoService {

    private final CatPeriodoRepository catPeriodoRepository;

    @Override
    public List<Periododto> obtenerPeriodos() {
        return catPeriodoRepository.findAll()
                .stream()
                .map(a -> new Periododto(a.getCvePeriodo(), a.getDescPeriodo()))
                .collect(Collectors.toList());
    }
}
