package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.Pasien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasienRepository extends JpaRepository<Pasien, Long> {

    Optional<Pasien> findByUserId(Long userId);

    Optional<Pasien> findByNik(String nik);

    Page<Pasien> findByNamaLengkapContainingIgnoreCaseOrNikContaining(String nama, String nik, Pageable pageable);
}
