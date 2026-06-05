package com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkCabesera;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkCabeseraService;
import com.edomex.kiliantRSP.Mapper.VistaHistoricoMapper.VistaHistoricoCabeseraMapper;
import com.edomex.kiliantRSP.repository.HistoricoGlinkRepository.HistoricoGlinkCabeseraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoGlinkCabeseraServiceImpl implements HistoricoGlinkCabeseraService {

    private final HistoricoGlinkCabeseraRepository repository;
    private final VistaHistoricoCabeseraMapper mapper;

    @Override
    public HistoricoGlinkCabesera obtenerCabesera(String neyemp, String negnom, String rfc) {
        try {

            var entidad = repository.buscarCabecera(neyemp);

            if (entidad == null) {
                throw new RuntimeException("No se encontró al servidor público");
            }

            return mapper.toDto(entidad);

        } catch (Exception e) {
            throw new RuntimeException("No se encontró al servidor público", e);
        }
    }
}
