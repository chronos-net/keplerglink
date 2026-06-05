package com.edomex.kiliantRSP.models.kddesx;

import com.edomex.kiliantRSP.models.Base.PlazaBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "Kddesx1998", schema = "public")
public class Kddesx1998 implements PlazaBase{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "cve_empleado")
    private Integer cve_empleado;

    @Column(name = "neyemp")
    private String neyemp;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "sec_plaza")
    private String sec_plaza;

    @Column(name = "plaza")
    private String plaza;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "ads")
    private String ads;

    @Column(name = "clave_lugar")
    private String clave_lugar;

    @Column(name = "clave_centro")
    private String clave_centro;

    @Column(name = "estatus_plaza")
    private String estatus_plaza;

    @Column(name = "sindicato")
    private String sindicato;

    @Column(name = "tipo_nomina")
    private String tipo_nomina;

    @Column(name = "anio")
    private String anio;

}
