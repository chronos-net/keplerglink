package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "percepciones_plaza_glink", schema = "public")
public class Percepciones_Plaza_Glink {
    @Id
    @Column(name = "ftieje")
    private BigDecimal ftieje;


    @Column(name = "ownerid5")
    private Long ownerid5;


    @Column(name = "ztydep")
    private String ztydep;

    @Column(name = "ztyfol")
    private String ztyfol;

    @Column(name = "ftcper")
    private Integer ftcper;
}
