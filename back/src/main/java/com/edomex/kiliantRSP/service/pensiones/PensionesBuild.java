package com.edomex.kiliantRSP.service.pensiones;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.Pensiondto;

public interface PensionesBuild<T> {
    T build(Pensiondto raw);
}
