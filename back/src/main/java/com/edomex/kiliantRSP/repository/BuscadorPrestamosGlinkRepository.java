package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.Prestamos_Glink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BuscadorPrestamosGlinkRepository extends JpaRepository<Prestamos_Glink, String>{

    @Query(value = """
    SELECT DISTINCT ON (clavesp) *
    FROM prestamos_glink
    WHERE clavesp LIKE :filtro
    ORDER BY clavesp
    LIMIT 10
""", nativeQuery = true)
    List<Prestamos_Glink> findByClavesoLike(@Param("filtro") String filtro);
}
