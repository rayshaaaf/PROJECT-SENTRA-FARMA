package com.sentrafarma.pharmacy.service.impl;

import com.sentrafarma.pharmacy.entity.*;
import com.sentrafarma.pharmacy.payload.ResepCreateRequest;
import com.sentrafarma.pharmacy.payload.TransaksiCreateRequest;
import com.sentrafarma.pharmacy.repository.*;
import com.sentrafarma.pharmacy.service.PharmacyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final KategoriObatRepository kategoriRepository;
    private final ObatRepository obatRepository;
    private final ResepRepository resepRepository;
    private final TransaksiRepository transaksiRepository;

    public PharmacyServiceImpl(KategoriObatRepository kategoriRepository,
                               ObatRepository obatRepository,
                               ResepRepository resepRepository,
                               TransaksiRepository transaksiRepository) {
        this.kategoriRepository = kategoriRepository;
        this.obatRepository = obatRepository;
        this.resepRepository = resepRepository;
        this.transaksiRepository = transaksiRepository;
    }

    // --- Kategori Obat ---
    @Override
    public List<KategoriObat> getAllKategori() {
        return kategoriRepository.findAll();
    }

    @Override
    public KategoriObat createKategori(KategoriObat kat) {
        return kategoriRepository.save(kat);
    }

    // --- Inventory Obat ---
    @Override
    public Page<Obat> getObatList(int page, int limit, String search, Long kategoriId, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        if (search != null && !search.isBlank()) {
            return obatRepository.findByNamaObatContainingIgnoreCaseAndIsDeletedFalse(search, pageable);
        } else if (kategoriId != null) {
            return obatRepository.findByKategoriIdAndIsDeletedFalse(kategoriId, pageable);
        }
        return obatRepository.findByIsDeletedFalse(pageable);
    }

    @Override
    public Obat getObatById(Long id) {
        return obatRepository.findById(id)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Obat tidak ditemukan dengan ID: " + id));
    }

    @Override
    @Transactional
    public Obat saveObat(Obat obat) {
        if (obat.getKategori() != null && obat.getKategori().getId() != null) {
            kategoriRepository.findById(obat.getKategori().getId()).ifPresent(obat::setKategori);
        }
        if (obat.getKategori() == null || obat.getKategori().getId() == null) {
            KategoriObat defaultKat = kategoriRepository.findAll().stream().findFirst()
                    .orElseGet(() -> kategoriRepository.save(KategoriObat.builder().namaKategori("Obat Bebas").deskripsi("Obat Bebas").build()));
            obat.setKategori(defaultKat);
        }
        return obatRepository.save(obat);
    }

    @Override
    @Transactional
    public Obat updateObat(Long id, Obat updated) {
        Obat existing = getObatById(id);
        if (updated.getNamaObat() != null) {
            existing.setNamaObat(updated.getNamaObat());
        }
        if (updated.getKodeObat() != null) {
            existing.setKodeObat(updated.getKodeObat());
        }
        if (updated.getKategori() != null && updated.getKategori().getId() != null) {
            kategoriRepository.findById(updated.getKategori().getId()).ifPresent(existing::setKategori);
        }
        if (updated.getSatuan() != null) {
            existing.setSatuan(updated.getSatuan());
        }
        if (updated.getHarga() != null) {
            existing.setHarga(updated.getHarga());
        }
        if (updated.getStok() != null) {
            existing.setStok(updated.getStok());
        }
        if (updated.getMinStok() != null) {
            existing.setMinStok(updated.getMinStok());
        }
        if (updated.getTanggalKadaluarsa() != null) {
            existing.setTanggalKadaluarsa(updated.getTanggalKadaluarsa());
        }
        if (updated.getFotoUrl() != null) {
            existing.setFotoUrl(updated.getFotoUrl());
        }
        return obatRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteObat(Long id) {
        Obat existing = getObatById(id);
        existing.setIsDeleted(true);
        obatRepository.save(existing);
    }

    @Override
    public List<Obat> getLowStockItems() {
        return obatRepository.findLowStockItems();
    }

    // --- E-Resep ---
    @Override
    @Transactional
    public Resep createResep(ResepCreateRequest req) {
        Resep resep = Resep.builder()
                .rekamMedisId(req.getRekamMedisId())
                .pasienId(req.getPasienId())
                .dokterId(req.getDokterId())
                .status("PENDING")
                .isChatPrescription(req.getIsChatPrescription() != null ? req.getIsChatPrescription() : false)
                .items(new ArrayList<>())
                .build();

        for (ResepCreateRequest.ResepItemRequest itemReq : req.getItems()) {
            Obat obat = getObatById(itemReq.getObatId());
            DetailResep item = DetailResep.builder()
                    .resep(resep)
                    .obat(obat)
                    .jumlah(itemReq.getJumlah())
                    .aturanPakai(itemReq.getAturanPakai())
                    .build();
            resep.getItems().add(item);
        }

        return resepRepository.save(resep);
    }

    @Override
    public List<Resep> getResepByPasien(Long pasienId) {
        return resepRepository.findByPasienIdOrderByCreatedAtDesc(pasienId);
    }

    @Override
    public Page<Resep> getResepQueue(String status, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id").descending());
        return resepRepository.findByStatus(status, pageable);
    }

    @Override
    public List<Resep> getResepByStatus(String status) {
        return resepRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Resep fulfillResep(Long resepId) {
        Resep resep = resepRepository.findById(resepId)
                .orElseThrow(() -> new IllegalArgumentException("Resep tidak ditemukan"));

        if ("SELESAI".equalsIgnoreCase(resep.getStatus())) {
            throw new IllegalArgumentException("Resep sudah pernah diproses/ditransaksikan sebelumnya");
        }

        for (DetailResep detail : resep.getItems()) {
            Obat obat = detail.getObat();
            if (obat.getStok() < detail.getJumlah()) {
                throw new IllegalArgumentException("Stok obat '" + obat.getNamaObat() + "' tidak mencukupi! Sisa: " + obat.getStok());
            }
            obat.setStok(obat.getStok() - detail.getJumlah());
            obatRepository.save(obat);
        }

        resep.setStatus("SELESAI");
        return resepRepository.save(resep);
    }

    // --- Transaksi Kasir POS ---
    @Override
    @Transactional
    public Transaksi checkoutTransaksi(TransaksiCreateRequest req) {
        String invoiceNum = "INV-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + (int)(Math.random() * 900 + 100);

        Transaksi tx = Transaksi.builder()
                .nomorInvoice(invoiceNum)
                .resepId(req.getResepId())
                .pasienId(req.getPasienId())
                .kasirId(req.getKasirId())
                .metodePembayaran(req.getMetodePembayaran() != null ? req.getMetodePembayaran() : "CASH")
                .status("LUNAS")
                .totalHarga(0.0)
                .items(new ArrayList<>())
                .build();

        double grandTotal = 0.0;
        for (TransaksiCreateRequest.TransaksiItemRequest itemReq : req.getItems()) {
            Obat obat = getObatById(itemReq.getObatId());

            if (obat.getStok() < itemReq.getJumlah()) {
                throw new IllegalArgumentException("Stok obat '" + obat.getNamaObat() + "' tidak mencukupi!");
            }

            obat.setStok(obat.getStok() - itemReq.getJumlah());
            obatRepository.save(obat);

            double subtotal = obat.getHarga() * itemReq.getJumlah();
            grandTotal += subtotal;

            DetailTransaksi item = DetailTransaksi.builder()
                    .transaksi(tx)
                    .obat(obat)
                    .jumlah(itemReq.getJumlah())
                    .hargaSatuan(obat.getHarga())
                    .subtotal(subtotal)
                    .build();
            tx.getItems().add(item);
        }

        tx.setTotalHarga(grandTotal);

        if (req.getResepId() != null) {
            resepRepository.findById(req.getResepId()).ifPresent(r -> {
                r.setStatus("SELESAI");
                resepRepository.save(r);
            });
        }

        return transaksiRepository.save(tx);
    }

    @Override
    public Transaksi getTransaksiByInvoice(String invoice) {
        return transaksiRepository.findByNomorInvoice(invoice)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan dengan invoice: " + invoice));
    }

    @Override
    @Transactional
    public void resetHistory() {
        transaksiRepository.deleteAll();
        resepRepository.deleteAll();
    }
}
