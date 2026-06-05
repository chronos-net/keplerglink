package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "vista_pensiones_glink", schema = "public")
public class Vista_Pensiones {
    @Id
    @Column(name = "clavesp")
    private String clavesp;

    @Column(name = "nombresp")
    private String nombresp;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "fechain")
    private String fechain;

    @Column(name = "nombrepension")
    private String nombrepension;

    @Column(name = "tipo_desc")
    private String tipo_desc;

    @Column(name = "altaqna")
    private String altaqna;

    @Column(name = "porcentaje")
    private Integer porcentaje;

    @Column(name = "importe")
    private BigDecimal importe;

    @Column(name = "referencia")
    private String referencia;
}
