package com.edomex.kiliantRSP.Mapper;

import com.edomex.kiliantRSP.dto.BuscadorHistoricoGlinkDto.BuscadorHistoricoGlinkdto;
import com.edomex.kiliantRSP.models.Vista_Cabecera_Historico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VistaCabeseraHistoricoMapper {

    @Mapping(source = "cvesp", target = "neyemp")
    @Mapping(source = "nombre", target = "negnom")
    @Mapping(source = "rfc", target = "rfc")
    BuscadorHistoricoGlinkdto toDto(Vista_Cabecera_Historico entity);

    List<BuscadorHistoricoGlinkdto> toDtoList(List<Vista_Cabecera_Historico> entities);
}
