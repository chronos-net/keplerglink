package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.CatAnio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatAnioRepository extends JpaRepository<CatAnio, Integer> {
}
