package com.edomex.kiliantRSP.Mapper;

import com.edomex.kiliantRSP.dto.BuscadorPrestamosGlinkDto.BuscadorPrestamosGlinkdto;
import com.edomex.kiliantRSP.models.Prestamos_Glink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BuscadorPrestamosGlinkMapper {
    @Mapping(source = "clavesp", target = "neyemp")
    @Mapping(source = "nombre_sp", target = "negnom")
    @Mapping(source = "rfc", target = "rfc")
    BuscadorPrestamosGlinkdto toDto(Prestamos_Glink prestamos_Glink);

    List<BuscadorPrestamosGlinkdto> toDtoList(List<Prestamos_Glink> entities);
}
