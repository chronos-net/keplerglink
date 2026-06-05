package com.edomex.kiliantRSP.models.PuestosDependencias;

import com.edomex.kiliantRSP.models.Base.PuestoDependenciaBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_kddes_con_detalles2002", schema = "public")
public class Vw_kddes2002_con_detalles implements PuestoDependenciaBase {
    @Id
    @Column(name = "cve_empleado")
    private Integer cve_empleado;

    @Column(name = "neyemp")
    private String neyemp;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "plaza")
    private String plaza;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "leyenda_puesto")
    private String leyenda_puesto;

    @Column(name = "adsc")
    private String adsc;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "centro_trabajo")
    private String centro_trabajo;

    @Column(name = "sindicato")
    private String sindicato;

    @Column(name = "tipo_nomina")
    private String tipo_nomina;

    @Column(name = "anio")
    private String anio;

    @Column(name = "estatus_plaza")
    private String estatus_plaza;

    @Column(name = "lugar_pago")
    private String lugar_pago;

}
