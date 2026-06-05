package com.edomex.kiliantRSP.service.pensiones;

import com.edomex.kiliantRSP.dto.PensionDto.Pension2002_2022.Pension2002_2022;
import com.edomex.kiliantRSP.dto.PensionDto.PensionesPartes.Pensiondto;
import com.edomex.kiliantRSP.service.PensionService.PensionHistoricoServiceDesc;
import org.springframework.stereotype.Component;

@Component
public class PensionesBuilder2002_2022 implements PensionesBuild<Pension2002_2022>{

    private final PensionHistoricoServiceDesc pensionHistoricoServiceDesc;

    public PensionesBuilder2002_2022(PensionHistoricoServiceDesc pensionHistoricoServiceDesc) {
        this.pensionHistoricoServiceDesc = pensionHistoricoServiceDesc;
    }

    @Override
    public Pension2002_2022 build(Pensiondto dto){
        var pension = pensionHistoricoServiceDesc.obtenerPensionDesc(
                dto.neyemp(),
                dto.periodo(),
                dto.anio()
        );

        return new Pension2002_2022(pension);
    }
}
