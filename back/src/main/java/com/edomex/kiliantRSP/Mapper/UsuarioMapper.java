package com.edomex.kiliantRSP.Mapper;

import com.edomex.kiliantRSP.models.SbUsuario;
import com.edomex.kiliantRSP.models.CatUsuario;
import com.edomex.kiliantRSP.models.CatAdscripciones;
import com.edomex.kiliantRSP.dto.UsuarioDto;
import com.edomex.kiliantRSP.dto.CatUsuarioDto;
import com.edomex.kiliantRSP.dto.CatAdscripcionesDto;
import com.edomex.kiliantRSP.dto.CatUsuarioDto;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "cveUsuario", target = "cveUsuario")
    @Mapping(source = "tipoUsuario.cveTipoUsuario", target = "cveTipoUsuario")
    @Mapping(source = "tipoUsuario.descTipoUsuario", target = "descTipoUsuario")
    @Mapping(source = "adscripcion.cveAdscripcion", target = "cveAdscripcion")
    @Mapping(source = "adscripcion.claveAdscripcion", target = "claveAdscripcion")
    @Mapping(source = "adscripcion.descAdscripcion", target = "descAdscripcion")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "apellidoPaterno", target = "apellidoPaterno")
    @Mapping(source = "apellidoMaterno", target = "apellidoMaterno")
    @Mapping(source = "telefono", target = "telefono")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "centroDeTrabajo", target = "centroDeTrabajo")
    @Mapping(source = "usuarioName", target = "usuarioName")
    UsuarioDto toDto(SbUsuario usuario);
}
