package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cat_adscripciones", schema = "public")
public class CatAdscripciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_adscripcion")
    private Integer cveAdscripcion;

    @Column(name = "clave_adscripcion")
    private String claveAdscripcion;

    @Column(name = "desc_adscripcion")
    private String descAdscripcion;
}
