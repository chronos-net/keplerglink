package com.edomex.kiliantRSP.repository.PrestamosGlinkRepository;

import com.edomex.kiliantRSP.dto.PrestamosGlinkDto.PrestamosGlinkPartes.PrestamosGlinkDesc;
import com.edomex.kiliantRSP.models.Prestamos_Glink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamosGlinkDescRepository extends JpaRepository<Prestamos_Glink, String>{

    @Query(value = """
                    SELECT fecha_in, cve_ded, puesto, imp_total, 
                           imp_renta, saldo, plazos, qnas_x_pagar, doc_ref
                    FROM prestamos_glink
                    WHERE clavesp = :neyemp                
                    """, nativeQuery = true)
    List<PrestamosGlinkDesc> buscarDesc(@Param("neyemp") String neyemp);
}

