package com.edomex.kiliantRSP.service.descPlazas.builders;



import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.*;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DeduccionesPlazaDto;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Deducciones.DescPlazasDatosDeduccionesAnti;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.PercepcionesPlazaDto;

import com.edomex.kiliantRSP.dto.DescPlazasdto.DescPlazasPartes.Percepciones.DescPlazasDatosPercepcionesAnti;

import org.springframework.stereotype.Component;



import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.List;

import java.util.Optional;



@Component

public class Plazas1996Builder {



    public List<PlazasDto1996> build(

            List<PlazaBaseDto1996> bases,

            List<PercepcionesPlazaDto> percepciones,

            List<DeduccionesPlazaDto> deducciones

    ){



        return bases.stream()

                .map(base -> {



                    PercepcionesPlazaDto percepcion = fusionarPercepciones(base, percepciones);

                    DeduccionesPlazaDto deduccion = fusionarDeducciones(base, deducciones);



                    BigDecimal neto = calcularNeto(percepcion, deduccion);



                    return new PlazasDto1996(

                            base.plazaId(),

                            base.secuenciaPlaza(),

                            base.puesto(),

                            base.leyendaPuesto(),

                            base.lugpago(),

                            base.centroTrabajo(),

                            percepcion,

                            deduccion,

                            neto

                    );

                })

                .toList();

    }



    private PercepcionesPlazaDto fusionarPercepciones(

            PlazaBaseDto1996 base,

            List<PercepcionesPlazaDto> percepciones

    ){

        if (percepciones == null || percepciones.isEmpty()) return null;



        List<PercepcionesPlazaDto> bloques = percepciones.stream()

                .filter(p -> coincideSecuenciaPlaza(base.secuenciaPlaza(), p.secuenciaPlaza()))

                .toList();



        if (bloques.isEmpty()) return null;

        if (bloques.size() == 1) return bloques.get(0);



        List<DescPlazasDatosPercepcionesAnti> conceptos = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;



        for (PercepcionesPlazaDto bloque : bloques) {

            if (bloque.percepciones() != null) {

                conceptos.addAll(bloque.percepciones());

            }

            if (bloque.total() != null) {

                total = total.add(bloque.total());

            }

        }



        return new PercepcionesPlazaDto(

                base.secuenciaPlaza(),

                total.compareTo(BigDecimal.ZERO) > 0 ? total : null,

                conceptos

        );

    }



    private DeduccionesPlazaDto fusionarDeducciones(

            PlazaBaseDto1996 base,

            List<DeduccionesPlazaDto> deducciones

    ){

        if (deducciones == null || deducciones.isEmpty()) return null;



        List<DeduccionesPlazaDto> bloques = deducciones.stream()

                .filter(d -> coincideSecuenciaPlaza(base.secuenciaPlaza(), d.secuenciaPlaza()))

                .toList();



        if (bloques.isEmpty()) return null;

        if (bloques.size() == 1) return bloques.get(0);



        List<DescPlazasDatosDeduccionesAnti> conceptos = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;



        for (DeduccionesPlazaDto bloque : bloques) {

            if (bloque.deducciones() != null) {

                conceptos.addAll(bloque.deducciones());

            }

            if (bloque.total() != null) {

                total = total.add(bloque.total());

            }

        }



        return new DeduccionesPlazaDto(

                base.secuenciaPlaza(),

                total.compareTo(BigDecimal.ZERO) > 0 ? total : null,

                conceptos

        );

    }



    private boolean coincideSecuenciaPlaza(String secuenciaPlaza, String secuenciaConcepto) {

        if (secuenciaPlaza == null || secuenciaConcepto == null) {

            return false;

        }

        return secuenciaPlaza.trim().equals(secuenciaConcepto.trim());

    }



    private BigDecimal calcularNeto(

            PercepcionesPlazaDto percepcion,

            DeduccionesPlazaDto deduccion

    ){



        BigDecimal totalPercepciones = Optional.ofNullable(percepcion)

                .map(PercepcionesPlazaDto::total)

                .orElse(BigDecimal.ZERO);



        BigDecimal totalDeducciones = Optional.ofNullable(deduccion)

                .map(DeduccionesPlazaDto::total)

                .orElse(BigDecimal.ZERO);



        return totalPercepciones.subtract(totalDeducciones);

    }

}

