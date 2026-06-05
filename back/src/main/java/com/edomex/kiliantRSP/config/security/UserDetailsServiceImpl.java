package com.edomex.kiliantRSP.config.security;

import com.edomex.kiliantRSP.models.SbUsuario;
import com.edomex.kiliantRSP.repository.SbUsuarioRepository;
import com.edomex.kiliantRSP.models.CatUsuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SbUsuarioRepository sbUsuarioRepository;

    public UserDetailsServiceImpl(SbUsuarioRepository sbUsuarioRepository) {
        this.sbUsuarioRepository = sbUsuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SbUsuario usuario = sbUsuarioRepository.findByUsuarioNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        var authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().getDescTipoUsuario()));

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsuarioName(),
                usuario.getPassword(),
                authorities
        );
    }
}
