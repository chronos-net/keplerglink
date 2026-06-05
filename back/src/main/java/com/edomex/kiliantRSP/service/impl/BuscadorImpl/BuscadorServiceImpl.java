package com.edomex.kiliantRSP.service.impl.BuscadorImpl;

import com.edomex.kiliantRSP.Mapper.BuscadorMapper;
import com.edomex.kiliantRSP.dto.BuscadorDto.Buscadordto;
import com.edomex.kiliantRSP.repository.BuscadorRepository;
import com.edomex.kiliantRSP.service.BuscadoService.BuscadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscadorServiceImpl implements BuscadorService {

    private final BuscadorRepository buscadorRepository;
    private final BuscadorMapper buscadorMapper;

    //metodo principal
    @Override
    public List<Buscadordto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }

        String filtro = "%" + nombre.trim() + "%";

        var entidades = buscadorRepository.findByNombreLike(filtro);
        return buscadorMapper.toDtoList(entidades);
    }

}
