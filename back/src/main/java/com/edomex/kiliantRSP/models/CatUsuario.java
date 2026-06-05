package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cat_usuario", schema = "public")
public class CatUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_tipo_usuario")
    private int cveTipoUsuario;

    @Column(name = "desc_tipo_usuario")
    private String descTipoUsuario;
}
