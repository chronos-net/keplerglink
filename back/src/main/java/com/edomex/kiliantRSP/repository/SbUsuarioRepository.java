package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.SbUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SbUsuarioRepository extends JpaRepository<SbUsuario, Integer> {
    Optional<SbUsuario> findByUsuarioNameIgnoreCase(String usuarioName);
}