package com.edomex.kiliantRSP.repository.HistoricoGlinkRepository;

import com.edomex.kiliantRSP.models.Tabla_Pzaemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoGlinkPzaempRepository extends JpaRepository<Tabla_Pzaemp, String> {

    @Query(value = "SELECT * FROM tabla_pzaemp WHERE cvesp = :cvesp", nativeQuery = true)
    List<Object[]> obtenerDepFol(@Param("cvesp") String cvesp);
}
