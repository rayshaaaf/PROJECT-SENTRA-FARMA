package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.Dokter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DokterRepository extends JpaRepository<Dokter, Long> {

    Optional<Dokter> findByUserId(Long userId);

    List<Dokter> findByPoliklinikId(Long poliklinikId);

    List<Dokter> findByStatusDuty(String statusDuty);

    Page<Dokter> findByNamaDokterContainingIgnoreCase(String keyword, Pageable pageable);
}
