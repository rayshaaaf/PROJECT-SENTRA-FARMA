package com.sentrafarma.pharmacy.repository;

import com.sentrafarma.pharmacy.entity.Resep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResepRepository extends JpaRepository<Resep, Long> {

    List<Resep> findByPasienIdOrderByCreatedAtDesc(Long pasienId);

    List<Resep> findByDokterIdOrderByCreatedAtDesc(Long dokterId);

    Page<Resep> findByStatus(String status, Pageable pageable);

    Optional<Resep> findByRekamMedisId(Long rekamMedisId);
}
