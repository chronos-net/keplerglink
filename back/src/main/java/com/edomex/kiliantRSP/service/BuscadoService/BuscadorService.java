package com.edomex.kiliantRSP.service.BuscadoService;

import com.edomex.kiliantRSP.dto.BuscadorDto.Buscadordto;
import java.util.List;

public interface BuscadorService {
    List<Buscadordto> buscarPorNombre(String nombre);
}
