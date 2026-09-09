package com.sentrafarma.pharmacy.repository;

import com.sentrafarma.pharmacy.entity.Transaksi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {

    Optional<Transaksi> findByNomorInvoice(String nomorInvoice);

    Page<Transaksi> findByPasienId(Long pasienId, Pageable pageable);
}
