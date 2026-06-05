package com.edomex.kiliantRSP.service.ReciboService;

import com.edomex.kiliantRSP.dto.Recibodto.ReciboPartes.ReciboNumeroDto;

public interface ReciboNumeroService {

    ReciboNumeroDto obtenerNumero(String neyemp, int anio, String quincena);
}
