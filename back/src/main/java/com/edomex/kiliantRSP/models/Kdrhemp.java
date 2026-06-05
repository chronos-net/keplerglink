package com.edomex.kiliantRSP.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name =  "kdrhemp", schema = "public")
public class Kdrhemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "clave_del_empledo")
    private String claveDelEmpleado;


    @Column(name = "Fecha_de_Ingreso")
    private LocalDate fechaDeIngreso;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "Fecha_de_Nacimiento")
    private LocalDate fechaDeNacimiento;

    @Column(name = "pais_de_nacimieto")
    private String paisDeNacimiento;

    @Column(name = "estado_de_nacimto")
    private String estadoDeNacimiento;

    @Column(name = "municipio_de_nacimento")
    private String municipioDeNacimiento;

    @Column(name = "nacionalidad")
    private String nacionalidad;

    @Column(name = "nombre_del_padre")
    private String nombreDelPadre;

    @Column(name = "nombre_de_la_madre")
    private String nombreDeLaMadre;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "imss")
    private String imss;

    @Column(name = "curp")
    private String curp;

    @Column(name = "clave_de_usuario")
    private String claveDeUsuario;

    @Column(name = "estado_civil")
    private String estadoCivil;

    @Column(name = "grado_maximo_de_estudios")
    private String gradoMaximoDeEstudios;

    @Column(name = "calle_y_numero")
    private String calleYNumero;

    @Column(name = "colonia_o_poblado")
    private String coloniaOPoblado;

    @Column(name = "pais")
    private String pais;

    @Column(name = "estado")
    private String estado;

    @Column(name = "municipio")
    private String municipio;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "telefono_1")
    private String telefono1;

    @Column(name = "telefono_2")
    private String telefono2;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "sindicalizado")
    private Integer sindicalizado;

    @Column(name = "status")
    private String status;

    @Column(name = "Ultimo_Cambio")
    private LocalDateTime ultimoCambio;

    @Column(name = "tipo_de_empleado")
    private String tipoDeEmpleado;

    @Column(name = "Fecha_de_Vencimiento")
    private LocalDate fechaDeVencimiento;

    @Column(name = "forma_de_pago")
    private String formaDePago;

    @Column(name = "numero_de_cuenta")
    private String numeroDeCuenta;

    @Column(name = "cargos", precision = 15, scale = 2)
    private BigDecimal cargos;

    @Column(name = "abonos", precision = 15, scale = 2)
    private BigDecimal abonos;
}
