package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.JadwalDokter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JadwalDokterRepository extends JpaRepository<JadwalDokter, Long> {

    List<JadwalDokter> findByDokterId(Long dokterId);

    List<JadwalDokter> findByHari(String hari);
}
