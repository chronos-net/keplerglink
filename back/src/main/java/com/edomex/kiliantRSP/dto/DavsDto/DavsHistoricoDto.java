package com.edomex.kiliantRSP.dto.DavsDto;

import com.edomex.kiliantRSP.dto.DavsDto.*;
import com.edomex.kiliantRSP.service.impl.DavsImpl.Davs;

public record DavsHistoricoDto(
        DavsAsesoriaDto         davsAsesoriaDto,
        DavsTramiteDto          davsTramiteDto,
        DavsSolicitudDto        davsSolicitudDto,
        DavsEntregadoDto        davsEntregadoDto

) implements Davs{
}
