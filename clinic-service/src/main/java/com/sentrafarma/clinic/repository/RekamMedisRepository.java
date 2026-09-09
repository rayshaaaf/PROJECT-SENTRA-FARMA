package com.sentrafarma.clinic.repository;

import com.sentrafarma.clinic.entity.RekamMedis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RekamMedisRepository extends JpaRepository<RekamMedis, Long> {

    List<RekamMedis> findByPasienIdAndIsDeletedFalseOrderByCreatedAtDesc(Long pasienId);

    List<RekamMedis> findByDokterIdAndIsDeletedFalseOrderByCreatedAtDesc(Long dokterId);

    Page<RekamMedis> findByIsDeletedFalse(Pageable pageable);
}
