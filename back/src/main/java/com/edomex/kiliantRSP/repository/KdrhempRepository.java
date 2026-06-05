package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.Kdrhemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KdrhempRepository extends JpaRepository<Kdrhemp, Integer> {

    @Query("SELECT k FROM Kdrhemp k WHERE k.claveDelEmpleado = :claveSp")
    Optional<Kdrhemp> findByClaveDelEmpleado(@Param("claveSp") String claveSp);

}
