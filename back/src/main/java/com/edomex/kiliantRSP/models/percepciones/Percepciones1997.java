package com.edomex.kiliantRSP.models.percepciones;

import com.edomex.kiliantRSP.models.Base.PercepcionBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "vista_percepciones_1997", schema = "public")
public class Percepciones1997 implements PercepcionBase {
    @Id
    @Column(name = "cve_empleado")
    private Integer cve_empleado;

    @Column(name = "neyemp")
    private String neyemp;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "secuencia_plaza")
    private String secuencia_plaza;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "per1")
    private String per1;

    @Column(name = "per2")
    private String per2;

    @Column(name = "per3")
    private String per3;

    @Column(name = "per4")
    private String per4;

    @Column(name = "per5")
    private String per5;

    @Column(name = "per6")
    private String per6;

    @Column(name = "per7")
    private String per7;

    @Column(name = "per8")
    private String per8;

    @Column(name = "per9")
    private String per9;

    @Column(name = "per10")

    private String per10;

    @Column(name = "imp1")
    private BigDecimal imp1;

    @Column(name = "imp2")
    private BigDecimal imp2;

    @Column(name = "imp3")
    private BigDecimal imp3;

    @Column(name = "imp4")
    private BigDecimal imp4;

    @Column(name = "imp5")
    private BigDecimal imp5;

    @Column(name = "imp6")
    private BigDecimal imp6;

    @Column(name = "imp7")
    private BigDecimal imp7;

    @Column(name = "imp8")
    private BigDecimal imp8;

    @Column(name = "imp9")
    private BigDecimal imp9;

    @Column(name = "imp10")
    private BigDecimal imp10;

    @Column(name = "per1A")
    private String per1A;

    @Column(name = "per2A")
    private String per2A;

    @Column(name = "per3A")
    private String per3A;

    @Column(name = "per4A")
    private String per4A;

    @Column(name = "per5A")
    private String per5A;

    @Column(name = "per6A")
    private String per6A;

    @Column(name = "per7A")
    private String per7A;

    @Column(name = "per8A")
    private String per8A;

    @Column(name = "per9A")
    private String per9A;

    @Column(name = "per10A")
    private String per10A;

    @Column(name = "per11A")
    private String per11A;

    @Column(name = "per12A")
    private String per12A;

    @Column(name = "per13A")
    private String per13A;

    @Column(name = "per14A")
    private String per14A;

    @Column(name = "per15A")
    private String per15A;

    @Column(name = "per16A")
    private String per16A;

    @Column(name = "per17A")
    private String per17A;

    @Column(name = "per18A")
    private String per18A;

    @Column(name = "per19A")
    private String per19A;

    @Column(name = "per20A")
    private String per20A;

    @Column(name = "imp1A")
    private BigDecimal imp1A;

    @Column(name = "imp2A")
    private BigDecimal imp2A;

    @Column(name = "imp3A")
    private BigDecimal imp3A;

    @Column(name = "imp4A")
    private BigDecimal imp4A;

    @Column(name = "imp5A")
    private BigDecimal imp5A;

    @Column(name = "imp6A")
    private BigDecimal imp6A;

    @Column(name = "imp7A")
    private BigDecimal imp7A;

    @Column(name = "imp8A")
    private BigDecimal imp8A;

    @Column(name = "imp9A")
    private BigDecimal imp9A;

    @Column(name = "imp10A")
    private BigDecimal imp10A;

    @Column(name = "imp11A")
    private BigDecimal imp11A;

    @Column(name = "imp12A")
    private BigDecimal imp12A;

    @Column(name = "imp13A")
    private BigDecimal imp13A;

    @Column(name = "imp14A")
    private BigDecimal imp14A;

    @Column(name = "imp15A")
    private BigDecimal imp15A;

    @Column(name = "imp16A")
    private BigDecimal imp16A;

    @Column(name = "imp17A")
    private BigDecimal imp17A;

    @Column(name = "imp18A")
    private BigDecimal imp18A;

    @Column(name = "imp19A")
    private BigDecimal imp19A;

    @Column(name = "imp20A")
    private BigDecimal imp20A;

    @Column(name = "per1B")
    private String per1B;

    @Column(name = "per2B")
    private String per2B;

    @Column(name = "per3B")
    private String per3B;

    @Column(name = "per4B")
    private String per4B;

    @Column(name = "per5B")
    private String per5B;

    @Column(name = "per6B")
    private String per6B;

    @Column(name = "per7B")
    private String per7B;

    @Column(name = "per8B")
    private String per8B;

    @Column(name = "per9B")
    private String per9B;

    @Column(name = "per10B")
    private String per10B;

    @Column(name = "per11B")
    private String per11B;

    @Column(name = "per12B")
    private String per12B;

    @Column(name = "per13B")
    private String per13B;

    @Column(name = "per14B")
    private String per14B;

    @Column(name = "per15B")
    private String per15B;

    @Column(name = "per16B")
    private String per16B;

    @Column(name = "per17B")
    private String per17B;

    @Column(name = "per18B")
    private String per18B;

    @Column(name = "per19B")
    private String per19B;

    @Column(name = "per20B")
    private String per20B;

    @Column(name = "imp1B")
    private BigDecimal imp1B;

    @Column(name = "imp2B")
    private BigDecimal imp2B;

    @Column(name = "imp3B")
    private BigDecimal imp3B;

    @Column(name = "imp4B")
    private BigDecimal imp4B;

    @Column(name = "imp5B")
    private BigDecimal imp5B;

    @Column(name = "imp6B")
    private BigDecimal imp6B;

    @Column(name = "imp7B")
    private BigDecimal imp7B;

    @Column(name = "imp8B")
    private BigDecimal imp8B;

    @Column(name = "imp9B")
    private BigDecimal imp9B;

    @Column(name = "imp10B")
    private BigDecimal imp10B;

    @Column(name = "imp11B")
    private BigDecimal imp11B;

    @Column(name = "imp12B")
    private BigDecimal imp12B;

    @Column(name = "imp13B")
    private BigDecimal imp13B;

    @Column(name = "imp14B")
    private BigDecimal imp14B;

    @Column(name = "imp15B")
    private BigDecimal imp15B;

    @Column(name = "imp16B")
    private BigDecimal imp16B;

    @Column(name = "imp17B")
    private BigDecimal imp17B;

    @Column(name = "imp18B")
    private BigDecimal imp18B;

    @Column(name = "imp19B")
    private BigDecimal imp19B;

    @Column(name = "imp20B")
    private BigDecimal imp20B;
}
