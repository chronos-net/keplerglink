package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cat_anio", schema = "public")
public class CatAnio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_anio")
    private Integer cveAnio;

    @Column(name = "desc_anio", nullable = false, length = 10)
    private String descAnio;
}
