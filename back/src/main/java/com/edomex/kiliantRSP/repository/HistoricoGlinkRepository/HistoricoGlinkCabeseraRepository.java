package com.edomex.kiliantRSP.repository.HistoricoGlinkRepository;

import com.edomex.kiliantRSP.models.Vista_Cabecera_Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoGlinkCabeseraRepository extends JpaRepository<Vista_Cabecera_Historico, String> {

    @Query(value = """
            SELECT
                a.cvesp,
                a.rfc,
                a.nombre,
                a.sitsp,
                a.curp,
                a.dep,
                a.uresp,
                a.npza,
                a.cct,
                a.nh,
                a.catego,
                a.perdeocupacion,
                b.puesto AS puesto
            FROM vista_cabecera_historico a
            LEFT JOIN tabla_pzaemp b
                ON a.cvesp = b.cvesp
            WHERE a.cvesp = :neyemp
            """, nativeQuery = true)
    Vista_Cabecera_Historico buscarCabecera(@Param("neyemp") String neyemp);
}
