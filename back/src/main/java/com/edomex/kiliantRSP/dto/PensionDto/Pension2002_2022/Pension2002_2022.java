package com.edomex.kiliantRSP.dto.PensionDto.Pension2002_2022;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.*;
import com.edomex.kiliantRSP.service.impl.PensionImpl.Pension;

public record Pension2002_2022(
        PensionDesc pensionDesc
) implements Pension {
}
