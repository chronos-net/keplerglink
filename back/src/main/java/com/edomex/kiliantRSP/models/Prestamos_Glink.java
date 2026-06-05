package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "prestamos_glink", schema = "public")
public class Prestamos_Glink {
    @Id
    @Column(name = "clavesp")
    private String clavesp;

    @Column(name = "nombre_sp")
    private String nombre_sp;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "fecha_in")
    private String fecha_in;

    @Column(name = "cve_ded")
    private String cve_ded;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "imp_total")
    private BigDecimal imp_total;

    @Column(name = "imp_renta")
    private BigDecimal imp_renta;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "plazos")
    private BigDecimal plazos;

    @Column(name = "qnas_x_pagar")
    private BigDecimal qnas_x_pagar;

    @Column(name = "doc_ref")
    private String doc_ref;
}
