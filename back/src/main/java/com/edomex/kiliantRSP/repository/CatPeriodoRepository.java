package com.edomex.kiliantRSP.repository;

import com.edomex.kiliantRSP.models.CatPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatPeriodoRepository extends JpaRepository<CatPeriodo, Integer> {
}
