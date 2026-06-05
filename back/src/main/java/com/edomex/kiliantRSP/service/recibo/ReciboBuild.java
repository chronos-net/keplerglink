package com.edomex.kiliantRSP.service.recibo;

import com.edomex.kiliantRSP.dto.Recibodto.Recibodto;

public interface ReciboBuild<T> {
    T build(Recibodto raw);
}
