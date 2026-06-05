package com.edomex.kiliantRSP.models.deducciones;

import com.edomex.kiliantRSP.models.Base.DeduccionBase;
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
@Table(name = "vista_deducciones_2001", schema = "public")
public class Deducciones2001 implements DeduccionBase {
    @Id
    @Column(name = "cve_neyemp")
    private Integer cve_neyemp;

    @Column(name = "neyemp")
    private String neyemp;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "secuencia_plaza")
    private String secuencia_plaza;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "ded1")
    private String ded1;

    @Column(name = "ded2")
    private String ded2;

    @Column(name = "ded3")
    private String ded3;

    @Column(name = "ded4")
    private String ded4;

    @Column(name = "ded5")
    private String ded5;

    @Column(name = "ded6")
    private String ded6;

    @Column(name = "ded7")
    private String ded7;

    @Column(name = "ded8")
    private String ded8;

    @Column(name = "ded9")
    private String ded9;

    @Column(name = "ded10")
    private String ded10;

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

    @Column(name = "ded1A")
    private String ded1A;

    @Column(name = "ded2A")
    private String ded2A;

    @Column(name = "ded3A")
    private String ded3A;

    @Column(name = "ded4A")
    private String ded4A;

    @Column(name = "ded5A")
    private String ded5A;

    @Column(name = "ded6A")
    private String ded6A;

    @Column(name = "ded7A")
    private String ded7A;

    @Column(name = "ded8A")
    private String ded8A;

    @Column(name = "ded9A")
    private String ded9A;

    @Column(name = "ded10A")
    private String ded10A;

    @Column(name = "ded11A")
    private String ded11A;

    @Column(name = "ded12A")
    private String ded12A;

    @Column(name = "ded13A")
    private String ded13A;

    @Column(name = "ded14A")
    private String ded14A;

    @Column(name = "ded15A")
    private String ded15A;

    @Column(name = "ded16A")
    private String ded16A;

    @Column(name = "ded17A")
    private String ded17A;

    @Column(name = "ded18A")
    private String ded18A;

    @Column(name = "ded19A")
    private String ded19A;

    @Column(name = "ded20A")
    private String ded20A;

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

    @Column(name = "ded1B")
    private String ded1B;

    @Column(name = "ded2B")
    private String ded2B;

    @Column(name = "ded3B")
    private String ded3B;

    @Column(name = "ded4B")
    private String ded4B;

    @Column(name = "ded5B")
    private String ded5B;

    @Column(name = "ded6B")
    private String ded6B;

    @Column(name = "ded7B")
    private String ded7B;

    @Column(name = "ded8B")
    private String ded8B;

    @Column(name = "ded9B")
    private String ded9B;

    @Column(name = "ded10B")
    private String ded10B;

    @Column(name = "ded11B")
    private String ded11B;

    @Column(name = "ded12B")
    private String ded12B;

    @Column(name = "ded13B")
    private String ded13B;

    @Column(name = "ded14B")
    private String ded14B;

    @Column(name = "ded15B")
    private String ded15B;

    @Column(name = "ded16B")
    private String ded16B;

    @Column(name = "ded17B")
    private String ded17B;

    @Column(name = "ded18B")
    private String ded18B;

    @Column(name = "ded19B")
    private String ded19B;

    @Column(name = "ded20B")
    private String ded20B;

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
