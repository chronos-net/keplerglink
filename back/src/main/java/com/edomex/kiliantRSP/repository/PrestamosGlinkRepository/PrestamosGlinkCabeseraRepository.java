package com.edomex.kiliantRSP.repository.PrestamosGlinkRepository;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkCabesera;
import com.edomex.kiliantRSP.models.Prestamos_Glink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamosGlinkCabeseraRepository extends JpaRepository<Prestamos_Glink, Long>{

    @Query(value = """
                SELECT clavesp, nombre_sp, rfc
                FROM prestamos_glink
                WHERE clavesp = :neyemp
                LIMIT 1
            """, nativeQuery = true)
    Object buscarCabesera(@Param("neyemp") String neyemp);

}
