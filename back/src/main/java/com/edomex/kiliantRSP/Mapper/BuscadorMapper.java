package com.edomex.kiliantRSP.Mapper;

import com.edomex.kiliantRSP.dto.BuscadorDto.Buscadordto;
import com.edomex.kiliantRSP.models.Kdrhemp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BuscadorMapper {

    @Mapping(source = "nombreCompleto", target = "negnom")
    @Mapping(source = "claveDelEmpleado", target = "neyemp")
    Buscadordto toDto(Kdrhemp entity);

    List<Buscadordto> toDtoList(List<Kdrhemp> entities);
}
