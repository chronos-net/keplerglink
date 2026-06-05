package com.edomex.kiliantRSP.repository.HistoricoGlinkRepository;

import com.edomex.kiliantRSP.models.Percepciones_Plaza_Glink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface HistoricoGlinkPercepcionesRepository extends JpaRepository<Percepciones_Plaza_Glink, Long> {

    @Query(value = "SELECT SUM(ftieje) as total FROM percepciones_plaza_glink WHERE ztydep || ztyfol = :plaza", nativeQuery = true)
    BigDecimal obtenerTotal(@Param("plaza") String plaza);
}