package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.Antrian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AntrianRepository extends JpaRepository<Antrian, Long> {

    List<Antrian> findByTanggalBerobat(String tanggalBerobat);

    List<Antrian> findByTanggalBerobatAndDokterId(String tanggalBerobat, Long dokterId);

    List<Antrian> findByPasienIdOrderByCreatedAtDesc(Long pasienId);

    Page<Antrian> findByStatusAndTanggalBerobat(String status, String tanggalBerobat, Pageable pageable);

    long countByTanggalBerobatAndPoliklinikId(String tanggalBerobat, Long poliklinikId);
}
