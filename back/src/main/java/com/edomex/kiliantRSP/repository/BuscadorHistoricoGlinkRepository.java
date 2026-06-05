package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.Vista_Cabecera_Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuscadorHistoricoGlinkRepository
        extends JpaRepository<Vista_Cabecera_Historico, String> {

    @Query(value = """
            SELECT DISTINCT *
            FROM vista_cabecera_historico
            WHERE cvesp LIKE :filtro
            LIMIT 10
            """, nativeQuery = true)
    List<Vista_Cabecera_Historico> findByCvespLike(@Param("filtro") String filtro);
}