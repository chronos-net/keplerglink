package com.edomex.kiliantRSP.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tabla_pzaemp", schema = "public")
public class Tabla_Pzaemp {
    @Id
    @Column(name = "cvesp")
    private String cvesp;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "curp")
    private String curp;

    @Column(name = "fechainicial")
    private String fechainicial;

    @Column(name = "ctaban")
    private String ctaban;

    @Column(name = "sitservpublico")
    private String sitservpublico;

    @Column(name = "meservicio")
    private String meservicio;

    @Column(name = "dependencia")
    private String dependencia;

    @Column(name = "uresp")
    private String uresp;

    @Column(name = "nref")
    private String nref;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "cct")
    private String cct;

    @Column(name = "ztnhor")
    private String ztnhor;

    @Column(name = "fechaocup")
    private String fechaocup;

    @Column(name = "tipodeplaza")
    private String tipodeplaza;
}
