package com.sentrafarma.pharmacy.repository;

import com.sentrafarma.pharmacy.entity.KategoriObat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KategoriObatRepository extends JpaRepository<KategoriObat, Long> {

    Optional<KategoriObat> findByNamaKategori(String namaKategori);
}
