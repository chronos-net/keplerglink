package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.Kdrhemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BuscadorRepository extends JpaRepository<Kdrhemp, Integer> {

    @Query(value = """
            SELECT *
            FROM kdrhemp
            WHERE nombre_completo LIKE :nombre   
            LIMIT 10            
            """, nativeQuery = true)
    List<Kdrhemp> findByNombreLike(@Param("nombre") String nombre);
}
