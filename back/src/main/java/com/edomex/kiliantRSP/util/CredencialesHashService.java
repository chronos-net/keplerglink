package com.edomex.kiliantRSP.util;

import com.edomex.kiliantRSP.models.SbUsuario;
import com.edomex.kiliantRSP.repository.SbUsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CredencialesHashService {

    private final SbUsuarioRepository sbUsuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CredencialesHashService(SbUsuarioRepository sbUsuarioRepository) {
        this.sbUsuarioRepository = sbUsuarioRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public void actualizarContrasenas(){
        List<SbUsuario> usuarios = sbUsuarioRepository.findAll();

        for(SbUsuario usuario : usuarios){
            String rawPassword = usuario.getPassword();
            if(rawPassword == null && !rawPassword.isBlank()){
                String hash = bCryptPasswordEncoder.encode(rawPassword);
                usuario.setPassword(hash);
                sbUsuarioRepository.save(usuario);
            }

        }

        System.out.println("¡Todas las contraseñas han sido encriptadas!");
    }
}
