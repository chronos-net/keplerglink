package com.edomex.kiliantRSP.service.impl.BuscadorPrestamosGlinkImpl;

import com.edomex.kiliantRSP.Mapper.BuscadorPrestamosGlinkMapper;
import com.edomex.kiliantRSP.dto.BuscadorPrestamosGlinkDto.BuscadorPrestamosGlinkdto;
import com.edomex.kiliantRSP.repository.BuscadorPrestamosGlinkRepository;
import com.edomex.kiliantRSP.service.BuscadorPrestamamosGlinkService.BuscadorPrestamosGlinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscadorPrestamosGlinkImpl implements BuscadorPrestamosGlinkService {

    private final BuscadorPrestamosGlinkMapper buscadorPrestamosGlinkMapper;
    private final BuscadorPrestamosGlinkRepository buscadorPrestamosGlinkRepository;

    @Override
    public List<BuscadorPrestamosGlinkdto> buscadorPorNeyemp(@RequestParam("q") String clavesp){
        if(clavesp == null || clavesp.trim().isEmpty()){
            return List.of();
        }

        String filtro = "%" + clavesp.trim() + "%";

        var entidades = buscadorPrestamosGlinkRepository.findByClavesoLike(filtro);

        return buscadorPrestamosGlinkMapper.toDtoList(entidades);
    }
}
