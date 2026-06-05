package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.Vista_Pensiones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BuscadorPensionesGlinkRepository extends JpaRepository<Vista_Pensiones, String>{

    @Query(value = """
    SELECT DISTINCT ON (clavesp) * 
    FROM vista_pensiones_glink 
    WHERE clavesp LIKE :filtro
    ORDER BY clavesp 
    """, nativeQuery = true)

    List<Vista_Pensiones> findByClavesoLike(@Param("filtro") String filtro);
}
