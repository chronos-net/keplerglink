package com.edomex.kiliantRSP.Mapper;

import com.edomex.kiliantRSP.dto.BuscadorPensionesGlinkDto.BuscadorPensionesGlinkdto;
import com.edomex.kiliantRSP.models.Vista_Pensiones;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BuscadorPensionesGlinkMapper {
    @Mapping(source = "clavesp", target = "neyemp")
    @Mapping(source = "nombresp", target = "negnom")
    @Mapping(source = "rfc", target = "rfc")
    BuscadorPensionesGlinkdto toDto(Vista_Pensiones vistaPensiones);

    List<BuscadorPensionesGlinkdto> toDtoList(List<Vista_Pensiones> entities);

}
