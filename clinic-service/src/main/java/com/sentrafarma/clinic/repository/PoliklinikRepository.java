package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.Poliklinik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoliklinikRepository extends JpaRepository<Poliklinik, Long> {

    Optional<Poliklinik> findByNamaPoli(String namaPoli);
}
