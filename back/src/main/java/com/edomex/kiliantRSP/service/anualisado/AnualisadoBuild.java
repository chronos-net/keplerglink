package com.edomex.kiliantRSP.service.anualisado;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.Anualiasadodto;

public interface AnualisadoBuild<T> {
    T build(Anualiasadodto raw);
}
