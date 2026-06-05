package com.edomex.kiliantRSP.service.AnualisadoService;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.Anualiasadodto;
import com.edomex.kiliantRSP.service.impl.AnualisadoImpl.Anualisado;

public interface AnualisadoService {
    Anualisado getAnualisado(Anualiasadodto dto);
}
