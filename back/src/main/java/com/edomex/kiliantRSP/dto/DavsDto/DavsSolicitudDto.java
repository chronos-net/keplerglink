package com.edomex.kiliantRSP.dto.DavsDto;

import java.util.Date;

public record DavsSolicitudDto (
        Integer cveKdm1,
        String folioDocumento,
        String neyemp,
        String negnom,
        Date fecha,
        String destinatarioCheque
) {
}
