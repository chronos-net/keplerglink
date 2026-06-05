package com.edomex.kiliantRSP.models.Kddes;

import com.edomex.kiliantRSP.models.Base.PlazaDescBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kddes2002", schema = "public")
public class Kddes2002 implements PlazaDescBase{
    @Id
    @Column(name = "cve_empleado")
    private Integer cve_empleado;


    @Column(name = "neyemp")
    private String neyemp;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "secuencia_plaza")
    private String secuencia_plaza;

    @Column(name = "plaza")
    private String plaza;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "adsc")
    private String adsc;

    @Column(name = "lugar_pago")
    private String lugar_pago;

    @Column(name = "centro_trabajo")
    private String centro_trabajo;

    @Column(name = "estatus_plaza")
    private String estatus_plaza;

    @Column(name = "sindicato")
    private String sindicato;

    @Column(name = "tipo_nomina")
    private String tipo_nomina;

    @Column(name = "anio")
    private String anio;
}
