package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sb_usuario", schema = "public")
public class SbUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_usuario")
    private Integer cveUsuario;

    //relacionamos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_tipo_usuario", referencedColumnName = "cve_tipo_usuario")
    private CatUsuario tipoUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_adscripcion", referencedColumnName = "cve_adscripcion")
    private CatAdscripciones adscripcion;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "clave_servidor_publico")
    private String claveServidorPublico;

    @Column(name = "centro_de_trabajo")
    private String centroDeTrabajo;

    @Column(name = "usuario_name")
    private String usuarioName;

    @Column(name = "password")
    private String password;

    //hacemos el join
}
