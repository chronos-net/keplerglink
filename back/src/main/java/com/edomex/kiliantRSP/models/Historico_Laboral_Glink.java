package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "historico_laboral_glink", schema = "public")
public class Historico_Laboral_Glink {
    @Id
    @Column(name = "clavesp")
    private String clavesp;

    @Column(name = "dependencia")
    private String dependencia;

    @Column(name = "uresponsable")
    private String uresponsable;

    @Column(name = "plaza")
    private String plaza;

    @Column(name = "cct")
    private String cct;

    @Column(name = "nhoras")
    private String nhoras;

    @Column(name = "thoras")
    private String thoras;

    @Column(name = "catego")
    private String catego;

    @Column(name = "perocupacion")
    private String perocupacion;

    @Column(name = "totalpercepciones")
    private String totalpercepciones;

    @Column(name = "motivobaja")
    private String motivobaja;

    @Column(name = "tipodeplaza")
    private String tipodeplaza;
}
