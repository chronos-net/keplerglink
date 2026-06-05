package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cat_periodo", schema = "public")
public class CatPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_periodo")
    private Integer cvePeriodo;

    @Column(name = "desc_periodo", nullable = false, length = 10)
    private String descPeriodo;
}
