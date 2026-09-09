package com.sentrafarma.pharmacy.repository;

import com.sentrafarma.pharmacy.entity.Obat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ObatRepository extends JpaRepository<Obat, Long> {

    Optional<Obat> findByKodeObatAndIsDeletedFalse(String kodeObat);

    Page<Obat> findByIsDeletedFalse(Pageable pageable);

    Page<Obat> findByKategoriIdAndIsDeletedFalse(Long kategoriId, Pageable pageable);

    Page<Obat> findByNamaObatContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);

    @Query("SELECT o FROM Obat o WHERE o.isDeleted = false AND o.stok <= o.minStok")
    List<Obat> findLowStockItems();
}
