package com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.*;
import com.edomex.kiliantRSP.service.impl.PensionImpl.Pension;

import java.util.List;

public record PensionSimp(
        PensionCabeseraSimp pensionCabeseraSimp,
        List<PensionItem> pension
) {
}
