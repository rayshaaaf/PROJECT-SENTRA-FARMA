package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.TandaVital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TandaVitalRepository extends JpaRepository<TandaVital, Long> {

    Optional<TandaVital> findByRekamMedisId(Long rekamMedisId);
}
