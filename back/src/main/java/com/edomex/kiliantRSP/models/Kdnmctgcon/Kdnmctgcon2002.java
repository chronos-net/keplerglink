package com.edomex.kiliantRSP.models.Kdnmctgcon;

import  com.edomex.kiliantRSP.models.Base.DescPercepcionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "kdnmctgcon2002", schema = "public")
public class Kdnmctgcon2002 implements DescPercepcionBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_clave")
    private Integer cve_clave;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "clave")
    private String clave;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "anio")
    private String anio;

}
