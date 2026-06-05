package com.edomex.kiliantRSP.models.kdnom;

import com.edomex.kiliantRSP.models.Base.CantidadBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "kdnom2000", schema = "public")
public class Kdnom2000 implements CantidadBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_principal")
    private Integer cve_principal;  // ⚠ el nombre debe coincidir con getCve_principal()

    @Column(name = "neyemp")
    private String neyemp;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "cheque")
    private String cheque;

    @Column(name = "percep", precision = 15, scale = 4)
    private BigDecimal percep;

    @Column(name = "ded", precision = 15, scale = 4)
    private BigDecimal ded;

    @Column(name = "banco")
    private String banco;

    @Column(name = "num_cuenta")
    private String num_cuenta;

    @Column(name = "num_recibo")
    private String num_recibo;

    @Column(name = "lug_pago")
    private String lug_pago;

    @Column(name = "anio")
    private String anio;
}
