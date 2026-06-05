package com.edomex.kiliantRSP.controller;

import com.edomex.kiliantRSP.config.security.JwtUtil;
import com.edomex.kiliantRSP.dto.UsuarioDto;
import com.edomex.kiliantRSP.models.SbUsuario;
import com.edomex.kiliantRSP.repository.SbUsuarioRepository;
import com.edomex.kiliantRSP.Mapper.UsuarioMapper;
import com.edomex.kiliantRSP.util.Constants.ExceptionMessages;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import static com.edomex.kiliantRSP.util.Constants.ExceptionMessages.PASSWORD_NOT_FOUND;
import static com.edomex.kiliantRSP.util.Constants.ExceptionMessages.USER_NOT_FOUND;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "controlador encargado de validar, cerrar y consultar la array de session ")
public class AuthenticationController {

    //inyectamos los archivos a utilisar
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final SbUsuarioRepository sbUsuarioRepository;
    private final UsuarioMapper usuarioMapper;


    //constructor
    public AuthenticationController(
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            SbUsuarioRepository sbUsuarioRepository,
            UsuarioMapper usuarioMapper
    ){
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.sbUsuarioRepository = sbUsuarioRepository;
        this.usuarioMapper = usuarioMapper;

    }

    public record AuthRequest(@NotBlank String username, @NotBlank String password) {}

    public record AuthResponse(UsuarioDto usuario){}


    //funcion principal del login
    @PostMapping("/login")
    @Operation(summary = "api que recibe los parametros para el inicio de session", description = "Autentica al usuario y devuelve un JWT en cookie.")
    public ResponseEntity
            <AuthResponse>
            login(@RequestBody AuthRequest req,
                  HttpServletResponse response) {

        //realisamos la consulta al repo para econtrar el cve_usuario
        SbUsuario us = sbUsuarioRepository.findByUsuarioNameIgnoreCase(req.username())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));


        //validamos el password
        if(us.getPassword() == null || us.getPassword().isBlank()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionMessages.PASSWORD_NOT_FOUND);
        }

        //validamos el usuario y password
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        //creamos el token
        String token = jwtUtil.generateToken(req.username());

        ResponseCookie cookie = ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtUtil.getExpirationMs() / 1_000)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        UsuarioDto dto = usuarioMapper.toDto(us);
        return ResponseEntity.ok(new AuthResponse(dto));

    }

    //funcion me?
    @GetMapping("/me")
    public ResponseEntity<UsuarioDto> whoami(@AuthenticationPrincipal UserDetails principal) {
        SbUsuario user = sbUsuarioRepository.findByUsuarioNameIgnoreCase(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("USUARIO NO ENCONTRADO"));

        return ResponseEntity.ok(usuarioMapper.toDto(user));
    }



    //funcion princiapl para el cierre de la session
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

}
