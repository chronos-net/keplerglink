package com.edomex.kiliantRSP.Mapper.VistaHistoricoMapper;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkDesc;
import com.edomex.kiliantRSP.models.Vista_Cabecera_Historico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VistaHistoricoDescMapper {
    HistoricoGlinkDesc toDto(Vista_Cabecera_Historico entity);
}
