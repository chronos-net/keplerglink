package com.edomex.kiliantRSP.service.impl.PensionImpl;

import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.Pensiondto;
import com.edomex.kiliantRSP.dto.PensionDto.Pension1996_2001.Pension1996_2001;
import com.edomex.kiliantRSP.dto.PensionDto.Pension2002_2022.Pension2002_2022;
import com.edomex.kiliantRSP.service.PensionService.PensionService;
import com.edomex.kiliantRSP.service.pensiones.PensionesBuild;
import org.springframework.stereotype.Service;

@Service
public class PensionServiceImpl implements PensionService {

    private final PensionesBuild<Pension1996_2001> build1996_2001;
    private final PensionesBuild<Pension2002_2022> build2002_2022;

    public PensionServiceImpl(
            PensionesBuild<Pension1996_2001> build1996_2001,
            PensionesBuild<Pension2002_2022> build2002_2022
    ) {
        this.build1996_2001 = build1996_2001;
        this.build2002_2022 = build2002_2022;
    }

    @Override
    public Pension getPension(Pensiondto dto) {
        int anio = dto.anio();

        if (anio >= 1996 && anio <= 2001) {
            return build1996_2001.build(dto);
        } else if (anio >= 2002 && anio <= 2022) {
            return build2002_2022.build(dto);
        } else {
            throw new RuntimeException("No hay builder definido para el año " + anio);
        }
    }
}
