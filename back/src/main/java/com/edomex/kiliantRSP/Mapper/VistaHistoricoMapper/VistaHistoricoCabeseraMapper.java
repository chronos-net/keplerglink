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
    @Mapping(target = "desc_puesto", source = "puesto")
    @Mapping(target = "perdeocupacionFormato", expression = "java(FechaPeriodoHelper.formatearPeriodo(entidad.getPerdeocupacion()))")
    HistoricoGlinkCabesera toDto(Vista_Cabecera_Historico entidad);

    class FechaPeriodoHelper {

        static String formatearPeriodo(String periodo) {
            if (periodo == null) {
                return null;
            }

            if (periodo.isEmpty() || !periodo.contains("-")) {
                return periodo;
            }

            String[] partes = periodo.split("-");

            if (partes.length < 2) {
                return periodo;
            }

            String inicio = partes[0];
            String fin = partes[1];

            if (inicio.length() != 8 || fin.length() != 8) {
                return periodo;
            }

            String diaInicio = inicio.substring(0, 2);
            String mesInicio = inicio.substring(2, 4);
            String anioInicio = inicio.substring(4, 8);

            if (fin.equals("99999999")) {
                return "del día " + diaInicio +
                        " del mes " + mesInicio +
                        " del año " + anioInicio +
                        " - 99999999";
            }

            String diaFin = fin.substring(0, 2);
            String mesFin = fin.substring(2, 4);
            String anioFin = fin.substring(4, 8);

            return "del día " + diaInicio +
                    " del mes " + mesInicio +
                    " del año " + anioInicio +
                    " al día " + diaFin +
                    " del mes " + mesFin +
                    " del año " + anioFin;
        }
    }
}
