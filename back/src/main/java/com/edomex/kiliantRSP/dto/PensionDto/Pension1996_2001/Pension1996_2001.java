package com.edomex.kiliantRSP.dto.PensionDto.Pension1996_2001;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.*;
import com.edomex.kiliantRSP.service.impl.PensionImpl.Pension;

public record Pension1996_2001(
        PensionSimp pensionSimp
) implements Pension {
}
