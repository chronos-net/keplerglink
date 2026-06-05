package com.edomex.kiliantRSP.Mapper.DescPlazasMapper;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.DescPlazasDatosPercepcionesAnti;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.DescPlazasDatosPercepciones;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.DescPlazasDatosPercepcionesSimp;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.PercepcionesPlazaDto;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DatosPercepcionesPlazasMapper {

    default DescPlazasDatosPercepcionesSimp toDatosPercepcionesSimp(
            DescPlazasDatosPercepciones p){

        if (p == null) return null;

        List<DescPlazasDatosPercepcionesAnti> simples = new ArrayList<>();

        addIfValid(simples, p.per1(), p.imp1());
        addIfValid(simples, p.per2(), p.imp2());
        addIfValid(simples, p.per3(), p.imp3());
        addIfValid(simples, p.per4(), p.imp4());
        addIfValid(simples, p.per5(), p.imp5());
        addIfValid(simples, p.per6(), p.imp6());
        addIfValid(simples, p.per7(), p.imp7());
        addIfValid(simples, p.per8(), p.imp8());
        addIfValid(simples, p.per9(), p.imp9());
        addIfValid(simples, p.per10(), p.imp10());

        addIfValid(simples, p.per1A(), p.imp1A());
        addIfValid(simples, p.per2A(), p.imp2A());
        addIfValid(simples, p.per3A(), p.imp3A());
        addIfValid(simples, p.per4A(), p.imp4A());
        addIfValid(simples, p.per5A(), p.imp5A());
        addIfValid(simples, p.per6A(), p.imp6A());
        addIfValid(simples, p.per7A(), p.imp7A());
        addIfValid(simples, p.per8A(), p.imp8A());
        addIfValid(simples, p.per9A(), p.imp9A());
        addIfValid(simples, p.per10A(), p.imp10A());
        addIfValid(simples, p.per11A(), p.imp11A());
        addIfValid(simples, p.per12A(), p.imp12A());
        addIfValid(simples, p.per13A(), p.imp13A());
        addIfValid(simples, p.per14A(), p.imp14A());
        addIfValid(simples, p.per15A(), p.imp15A());
        addIfValid(simples, p.per16A(), p.imp16A());
        addIfValid(simples, p.per17A(), p.imp17A());
        addIfValid(simples, p.per18A(), p.imp18A());
        addIfValid(simples, p.per19A(), p.imp19A());
        addIfValid(simples, p.per20A(), p.imp20A());

        addIfValid(simples, p.per1B(), p.imp1B());
        addIfValid(simples, p.per2B(), p.imp2B());
        addIfValid(simples, p.per3B(), p.imp3B());
        addIfValid(simples, p.per4B(), p.imp4B());
        addIfValid(simples, p.per5B(), p.imp5B());
        addIfValid(simples, p.per6B(), p.imp6B());
        addIfValid(simples, p.per7B(), p.imp7B());
        addIfValid(simples, p.per8B(), p.imp8B());
        addIfValid(simples, p.per9B(), p.imp9B());
        addIfValid(simples, p.per10B(), p.imp10B());
        addIfValid(simples, p.per11B(), p.imp11B());
        addIfValid(simples, p.per12B(), p.imp12B());
        addIfValid(simples, p.per13B(), p.imp13B());
        addIfValid(simples, p.per14B(), p.imp14B());
        addIfValid(simples, p.per15B(), p.imp15B());
        addIfValid(simples, p.per16B(), p.imp16B());
        addIfValid(simples, p.per17B(), p.imp17B());
        addIfValid(simples, p.per18B(), p.imp18B());
        addIfValid(simples, p.per19B(), p.imp19B());
        addIfValid(simples, p.per20B(), p.imp20B());

        return new DescPlazasDatosPercepcionesSimp(
                p.secuenciaPlaza(),
                (p.total() != null && p.total().compareTo(BigDecimal.ZERO) > 0) ? p.total() : null,
                simples
        );
    }

    private void addIfValid(List<DescPlazasDatosPercepcionesAnti> simples,
                            String per,
                            BigDecimal imp) {

        if (per != null && !per.isBlank() && imp != null && imp.compareTo(BigDecimal.ZERO) > 0){
            simples.add(new DescPlazasDatosPercepcionesAnti(
                    per, imp
            ));
        }
    }

    default List<DescPlazasDatosPercepcionesSimp> toDatosPercepcionesSimpList(
            List<DescPlazasDatosPercepciones> percepciones) {

        return percepciones.stream().map(p -> toDatosPercepcionesSimp(p))
                .toList();
    }

    default PercepcionesPlazaDto toPercepcionesPlazaDto(
            DescPlazasDatosPercepcionesSimp simp){

        if (simp == null) return null;

        return new PercepcionesPlazaDto(
                simp.secuenciaPlaza(),
                simp.total(),
                simp.percepciones()
        );
    }

    default List<PercepcionesPlazaDto> toPercepcionesPlazaDtoList(
            List<DescPlazasDatosPercepcionesSimp> list){

        return list.stream()
                .map(this::toPercepcionesPlazaDto)
                .toList();
    }


}
