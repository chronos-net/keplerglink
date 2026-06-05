package com.edomex.kiliantRSP.service.impl.BuscadorHistoricoGlinkImpl;

import com.edomex.kiliantRSP.Mapper.VistaCabeseraHistoricoMapper;
import com.edomex.kiliantRSP.dto.BuscadorHistoricoGlinkDto.BuscadorHistoricoGlinkdto;
import com.edomex.kiliantRSP.repository.BuscadorHistoricoGlinkRepository;
import com.edomex.kiliantRSP.service.BuscadorHistoricoGlinkService.BuscadorHistoricoGlinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscadorHistoricoGlinkServiceImpl implements BuscadorHistoricoGlinkService {

    private final BuscadorHistoricoGlinkRepository vistaCabeseraHistoricoRepository;
    private final VistaCabeseraHistoricoMapper vistaCabeseraHistoricoMapper;

    private static final Logger log = LoggerFactory.getLogger(BuscadorHistoricoGlinkServiceImpl.class);

    @Override
    public List<BuscadorHistoricoGlinkdto> buscadorPorNeyemp(String cvesp) {

        if (cvesp == null || cvesp.trim().isEmpty()) {
            return List.of();
        }

        String filtro = "%" + cvesp.trim() + "%";

        try {

            var entidades = vistaCabeseraHistoricoRepository.findByCvespLike(filtro);

            return vistaCabeseraHistoricoMapper.toDtoList(entidades);

        } catch (Exception e) {

            log.error("Error ejecutando query BuscadorHistoricoGlink", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al consultar el histórico"
            );
        }
    }
}