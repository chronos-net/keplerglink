package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vista_cabecera_historico", schema = "public")
public class Vista_Cabecera_Historico {

    @Id
    @Column(name = "cvesp")
    private String cvesp;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "sitsp")
    private String sitsp;

    @Column(name = "curp")
    private String curp;

    @Column(name = "dep")
    private String dep;

    @Column(name = "uresp")
    private String uresp;

    @Column(name = "npza")
    private String npza;

    @Column(name = "cct")
    private String CCT;

    @Column(name = "nh")
    private Integer nh;

    @Column(name = "catego")
    private String catego;

    @Column(name = "perdeocupacion")
    private String perdeocupacion;

    @Column(name = "puesto")
    private String puesto;

}
