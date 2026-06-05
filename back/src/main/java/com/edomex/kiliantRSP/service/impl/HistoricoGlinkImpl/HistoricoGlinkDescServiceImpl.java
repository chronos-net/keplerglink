package com.edomex.kiliantRSP.service.impl.HistoricoGlinkImpl;

import com.edomex.kiliantRSP.dto.HistoricoGlinkDto.HistoricoGlinkPartes.HistoricoGlinkDesc;
import com.edomex.kiliantRSP.repository.HistoricoGlinkRepository.HistoricoGlinkDescRepository;
import com.edomex.kiliantRSP.service.HistoricoGlinkService.HistoricoGlinkDescService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoGlinkDescServiceImpl implements HistoricoGlinkDescService {

    private final HistoricoGlinkDescRepository repository;

    @Override
    public List<HistoricoGlinkDesc> obtenerDescripciones(String cveEmpleado) {

        List<Object[]> raw = repository.obtenerHistoricoRaw(cveEmpleado);

        return raw.stream().map(r -> new HistoricoGlinkDesc(
                r[0] != null ? r[0].toString() : null,
                r[1] != null ? r[1].toString() : null,
                r[2] != null ? r[2].toString() : null,
                r[3] != null ? r[3].toString() : null,
                r[4] != null ? r[4].toString() : null,
                r[5] != null ? r[5].toString() : null,
                r[6] != null ? r[6].toString() : null,
                r[7] != null ? r[7].toString() : null,

                // 👇 AQUÍ transformamos perocupacion
                formatearPeriodoOcupacion(r[8] != null ? r[8].toString() : null),

                r[9] != null ? r[9].toString() : null,
                r[10] != null ? r[10].toString() : null,
                r[11] != null ? r[11].toString() : null,
                r[12] != null ? r[12].toString() : null,
                r[13] != null ? r[13].toString() : null
        )).toList();
    }

    // 🔥 Método helper limpio y reutilizable
    private String formatearPeriodoOcupacion(String perocupacion) {

        if (perocupacion == null || !perocupacion.contains("-")) {
            return perocupacion;
        }

        try {
            String[] partes = perocupacion.split("-");

            if (partes.length != 2) return perocupacion;

            String inicio = partes[0]; // 198617
            String fin = partes[1];    // 199217

            if (inicio.length() < 6 || fin.length() < 6) return perocupacion;

            String anioInicio = inicio.substring(0, 4);
            String periodoInicio = inicio.substring(4);

            String anioFin = fin.substring(0, 4);
            String periodoFin = fin.substring(4);

            return anioInicio + " periodo " + periodoInicio +
                    " al " +
                    anioFin + " periodo " + periodoFin;

        } catch (Exception e) {
            return perocupacion; // fallback seguro
        }
    }
}

