package com.edomex.kiliantRSP.service.pensiones;

import com.edomex.kiliantRSP.dto.PensionDto.Pension1996_2001.Pension1996_2001;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.Pensiondto;
import com.edomex.kiliantRSP.service.PensionService.PensionHistoricoServiceSimp;
import org.springframework.stereotype.Component;

@Component
public class PensionesBuilder1996_2001 implements PensionesBuild<Pension1996_2001> {

    private final PensionHistoricoServiceSimp pensionHistoricoServiceSimp;

    public PensionesBuilder1996_2001(PensionHistoricoServiceSimp pensionHistoricoServiceSimp) {
        this.pensionHistoricoServiceSimp = pensionHistoricoServiceSimp;
    }

    @Override
    public Pension1996_2001 build(Pensiondto dto) {
        var pension = pensionHistoricoServiceSimp.obtenerPensionSimp(
                dto.neyemp(),
                dto.periodo(),
                dto.anio()
        );

        return new Pension1996_2001(pension);
    }
}
