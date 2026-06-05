package com.edomex.kiliantRSP.dto;

public record UsuarioDto(
        Integer cveUsuario,
        Integer cveTipoUsuario,
        Integer cveAdscripcion,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String telefono,
        String email,
        String centroDeTrabajo,
        String usuarioName,
        String descTipoUsuario,
        String claveAdscripcion,
        String descAdscripcion
) {}