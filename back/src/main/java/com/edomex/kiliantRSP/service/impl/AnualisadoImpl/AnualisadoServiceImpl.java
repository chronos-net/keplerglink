package com.edomex.kiliantRSP.service.impl.AnualisadoImpl;

import com.edomex.kiliantRSP.dto.Anualizadodto.AnualisadoPartes.Anualiasadodto;
import com.edomex.kiliantRSP.dto.Anualizadodto.Anualisado1996_2001.Anualisado1996_2001;
import com.edomex.kiliantRSP.dto.Anualizadodto.Anualisado2002_2010.Anualisado2002_2011;
import com.edomex.kiliantRSP.service.AnualisadoService.AnualisadoService;
import com.edomex.kiliantRSP.service.anualisado.AnualisadoBuild;
import org.springframework.stereotype.Service;

@Service
public class AnualisadoServiceImpl implements AnualisadoService {

    private final AnualisadoBuild<Anualisado1996_2001> builder1996_2001;
    private final AnualisadoBuild<Anualisado2002_2011> builder2002_2011;

    public AnualisadoServiceImpl(
            AnualisadoBuild<Anualisado1996_2001> builder1996_2001,
            AnualisadoBuild<Anualisado2002_2011> builder2002_2011
    ) {
        this.builder1996_2001 = builder1996_2001;
        this.builder2002_2011 = builder2002_2011;
    }

    @Override
    public Anualisado getAnualisado(Anualiasadodto dto) {
        int anio = dto.anio();

        if(anio >= 1996 && anio <= 2001) {
            return builder1996_2001.build(dto);
        } else if (anio >= 2002 && anio <= 2023) {
          return builder2002_2011.build(dto);
        } else {
            throw new RuntimeException("No hay builder definido para el año " + anio);
        }

    }

}
