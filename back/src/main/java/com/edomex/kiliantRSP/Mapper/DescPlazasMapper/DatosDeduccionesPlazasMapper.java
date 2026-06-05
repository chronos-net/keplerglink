package com.edomex.kiliantRSP.Mapper.DescPlazasMapper;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DeduccionesPlazaDto;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeduccionesSimp;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeduccionesAnti;
import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeducciones;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DatosDeduccionesPlazasMapper {

    default DescPlazasDatosDeduccionesSimp toDatosDeduccionesSimp(
            DescPlazasDatosDeducciones p
    ){
         if (p == null) return null;

         List<DescPlazasDatosDeduccionesAnti> simples = new ArrayList<>();

        addIfValid(simples, p.ded1(), p.imp1());
        addIfValid(simples, p.ded2(), p.imp2());
        addIfValid(simples, p.ded3(), p.imp3());
        addIfValid(simples, p.ded4(), p.imp4());
        addIfValid(simples, p.ded5(), p.imp5());
        addIfValid(simples, p.ded6(), p.imp6());
        addIfValid(simples, p.ded7(), p.imp7());
        addIfValid(simples, p.ded8(), p.imp8());
        addIfValid(simples, p.ded9(), p.imp9());
        addIfValid(simples, p.ded10(), p.imp10());

        addIfValid(simples, p.ded1A(), p.imp1A());
        addIfValid(simples, p.ded2A(), p.imp2A());
        addIfValid(simples, p.ded3A(), p.imp3A());
        addIfValid(simples, p.ded4A(), p.imp4A());
        addIfValid(simples, p.ded5A(), p.imp5A());
        addIfValid(simples, p.ded6A(), p.imp6A());
        addIfValid(simples, p.ded7A(), p.imp7A());
        addIfValid(simples, p.ded8A(), p.imp8A());
        addIfValid(simples, p.ded9A(), p.imp9A());
        addIfValid(simples, p.ded10A(), p.imp10A());
        addIfValid(simples, p.ded11A(), p.imp11A());
        addIfValid(simples, p.ded12A(), p.imp12A());
        addIfValid(simples, p.ded13A(), p.imp13A());
        addIfValid(simples, p.ded14A(), p.imp14A());
        addIfValid(simples, p.ded15A(), p.imp15A());
        addIfValid(simples, p.ded16A(), p.imp16A());
        addIfValid(simples, p.ded17A(), p.imp17A());
        addIfValid(simples, p.ded18A(), p.imp18A());
        addIfValid(simples, p.ded19A(), p.imp19A());
        addIfValid(simples, p.ded20A(), p.imp20A());

        addIfValid(simples, p.ded1B(), p.imp1B());
        addIfValid(simples, p.ded2B(), p.imp2B());
        addIfValid(simples, p.ded3B(), p.imp3B());
        addIfValid(simples, p.ded4B(), p.imp4B());
        addIfValid(simples, p.ded5B(), p.imp5B());
        addIfValid(simples, p.ded6B(), p.imp6B());
        addIfValid(simples, p.ded7B(), p.imp7B());
        addIfValid(simples, p.ded8B(), p.imp8B());
        addIfValid(simples, p.ded9B(), p.imp9B());
        addIfValid(simples, p.ded10B(), p.imp10B());
        addIfValid(simples, p.ded11B(), p.imp11B());
        addIfValid(simples, p.ded12B(), p.imp12B());
        addIfValid(simples, p.ded13B(), p.imp13B());
        addIfValid(simples, p.ded14B(), p.imp14B());
        addIfValid(simples, p.ded15B(), p.imp15B());
        addIfValid(simples, p.ded16B(), p.imp16B());
        addIfValid(simples, p.ded17B(), p.imp17B());
        addIfValid(simples, p.ded18B(), p.imp18B());
        addIfValid(simples, p.ded19B(), p.imp19B());
        addIfValid(simples, p.ded20B(), p.imp20B());

        return new DescPlazasDatosDeduccionesSimp(
                p.secuenciaPlaza(),
                (p.total() != null && p.total().compareTo(BigDecimal.ZERO) > 0) ? p.total() : null,
                simples
        );
    }

    private void addIfValid(List<DescPlazasDatosDeduccionesAnti> simples,
                            String ded,
                            BigDecimal imp){
        if (ded != null && !ded.isBlank() && imp != null && imp.compareTo(BigDecimal.ZERO) > 0){
            simples.add(new DescPlazasDatosDeduccionesAnti(
                    ded,
                    imp
            ));
        }
    }

    default List<DescPlazasDatosDeduccionesSimp> toDatosDeduccionesSimpList(
            List<DescPlazasDatosDeducciones> deducciones){

        return  deducciones.stream().map(p -> toDatosDeduccionesSimp(p))
                .toList();
    }

    default DeduccionesPlazaDto toDeduccionesPlazaDto(
            DescPlazasDatosDeduccionesSimp simp){

        if (simp == null) return null;

        return new DeduccionesPlazaDto(
                simp.secuenciaPlaza(),
                simp.total(),
                simp.deducciones()
        );
    }

    default List<DeduccionesPlazaDto> toDeduccionesPlazaDtoList(
            List<DescPlazasDatosDeduccionesSimp> list){

        if (list == null) return List.of();

        return list.stream()
                .map(this::toDeduccionesPlazaDto)
                .toList();
    }



}
