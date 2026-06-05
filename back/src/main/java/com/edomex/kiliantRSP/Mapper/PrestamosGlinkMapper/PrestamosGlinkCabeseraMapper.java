package com.edomex.kiliantRSP.Mapper.PrestamosGlinkMapper;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkCabesera;
import com.edomex.kiliantRSP.models.Prestamos_Glink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestamosGlinkCabeseraMapper {
    @Mapping(target = "neyemp", source = "clavesp")
    @Mapping(target = "negnom", source = "nombre_sp")
    @Mapping(target = "rfc", source = "rfc")
    @Mapping(target = "fecha_in", source = "fecha_in")
    PrestamosGlinkCabesera toDto(Prestamos_Glink entidad);

}
