package com.edomex.kiliantRSP.service.PensionService;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.Pensiondto;
import com.edomex.kiliantRSP.service.impl.PensionImpl.Pension;

public interface PensionService {
    Pension getPension(Pensiondto dto);
}
