package com.edomex.kiliantRSP.service.impl.BuscadorPensionesGlinkImpl;

import com.edomex.kiliantRSP.Mapper.BuscadorPensionesGlinkMapper;
import com.edomex.kiliantRSP.dto.BuscadorPensionesGlinkDto.BuscadorPensionesGlinkdto;
import com.edomex.kiliantRSP.repository.BuscadorPensionesGlinkRepository;
import com.edomex.kiliantRSP.service.BuscadorPensionesGlinkService.BuscadorPensionesGlinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscadorPensionesGlinkImpl implements BuscadorPensionesGlinkService {

    private final BuscadorPensionesGlinkMapper buscadorPensionesGlinkMapper;
    private final BuscadorPensionesGlinkRepository buscadorPensionesGlinkRepository;

    @Override
    public List<BuscadorPensionesGlinkdto> buscarPorNeyemp(@RequestParam("q") String clavesp) {
        if(clavesp==null || clavesp.trim().isEmpty()){
            return List.of();
        }

        String filtro = "%" + clavesp.trim() + "%";

        var entidades = buscadorPensionesGlinkRepository.findByClavesoLike(filtro);

        return buscadorPensionesGlinkMapper.toDtoList(entidades);
    }
}
