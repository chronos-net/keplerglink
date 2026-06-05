package com.edomex.kiliantRSP.service.DescPlazasService;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.DescPlazasdto;
import com.edomex.kiliantRSP.service.impl.DescPlazasImpl.DescPlazas;

public interface DescPlazasService {
    DescPlazas getDescPlazas(DescPlazasdto dto);
}
