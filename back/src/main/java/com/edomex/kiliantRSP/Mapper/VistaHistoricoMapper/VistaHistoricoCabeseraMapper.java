package com.edomex.kiliantRSP.Mapper.VistaHistoricoMapper;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkCabesera;
import com.edomex.kiliantRSP.models.Vista_Cabecera_Historico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VistaHistoricoCabeseraMapper {

    @Mapping(target = "neyemp", source = "cvesp")
    @Mapping(target = "negnom", source = "nombre")
    @Mapping(target = "rfc", source = "rfc")
    @Mapping(target = "sitsp", source = "sitsp")
    @Mapping(target = "curp", source = "curp")
    @Mapping(target = "dep", source = "dep")
    @Mapping(target = "uresp", source = "uresp")
    @Mapping(target = "npza", source = "npza")
    @Mapping(target = "cct", source = "CCT")
    @Mapping(target = "nh", source = "nh")
    @Mapping(target = "catego", source = "catego")
    @Mapping(target = "perdeocupacion", source = "perdeocupacion")
    HistoricoGlinkCabesera toDto(Vista_Cabecera_Historico entidad);
}
