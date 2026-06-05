package com.edomex.kiliantRSP.service.PensionesGlinkService;

import com.edomex.kiliantRSP.dto.PensionesGlinkDto.PensionesGlinkDto;
import com.edomex.kiliantRSP.dto.PensionesGlinkDto.PensionesGlinkResponse;

import java.util.List;

public interface PensionesGlinkService {
    List<PensionesGlinkResponse> consultarPensiones(PensionesGlinkDto dto);
}
