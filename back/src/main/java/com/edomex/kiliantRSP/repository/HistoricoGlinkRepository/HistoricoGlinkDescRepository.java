package com.edomex.kiliantRSP.repository.HistoricoGlinkRepository;

import com.edomex.kiliantRSP.models.Historico_Laboral_Glink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoGlinkDescRepository extends JpaRepository<Historico_Laboral_Glink, Long> {

    @Query(value = """
        SELECT 
            A.clavesp,
            A.dependencia,
            A.uresponsable,
            A.plaza,
            A.cct,
            A.nhoras,
            A.thoras,
            A.catego,
            A.perocupacion,
            A.totalpercepciones AS totalpercep,
            A.motivobaja,
            A.tipodeplaza,
            B.idmb,
            B.descripcion
        FROM historico_laboral_glink A
        LEFT JOIN cat_mb B ON A.motivobaja = B.idmb
        WHERE A.clavesp = :cve
        ORDER BY A.dependencia
        """,
            nativeQuery = true)
    List<Object[]> obtenerHistoricoRaw(@Param("cve") String cveEmpleado);
}
