package com.sentrafarma.pharmacy.controller;

import com.sentrafarma.pharmacy.entity.*;
import com.sentrafarma.pharmacy.payload.ResepCreateRequest;
import com.sentrafarma.pharmacy.payload.TransaksiCreateRequest;
import com.sentrafarma.pharmacy.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pharmacy")
@Tag(name = "Pharmacy Service", description = "Endpoint Inventory Obat, E-Resep, Transaksi Kasir POS, & Notifikasi Stok Kritis")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    // --- Kategori Obat ---
    @GetMapping("/kategori")
    @Operation(summary = "Get List Kategori Obat")
    public ResponseEntity<List<KategoriObat>> getKategori() {
        return ResponseEntity.ok(pharmacyService.getAllKategori());
    }

    @PostMapping("/kategori")
    @Operation(summary = "Tambah Kategori Obat")
    public ResponseEntity<KategoriObat> createKategori(@RequestBody KategoriObat kat) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.createKategori(kat));
    }

    // --- Inventory Obat ---
    @GetMapping("/obat")
    @Operation(summary = "Get List Inventory Obat dengan Search, Filter Kategori, Sorting, & Pagination")
    public ResponseEntity<Page<Obat>> getObatList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long kategoriId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(pharmacyService.getObatList(page, limit, search, kategoriId, sortBy, sortDir));
    }

    @GetMapping("/obat/{id}")
    @Operation(summary = "Get Detail Obat by ID")
    public ResponseEntity<Obat> getObatById(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getObatById(id));
    }

    @PostMapping("/obat")
    @Operation(summary = "Tambah Item Obat Baru")
    public ResponseEntity<Obat> createObat(@RequestBody Obat obat) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.saveObat(obat));
    }

    @PutMapping("/obat/{id}")
    @Operation(summary = "Update Data Obat")
    public ResponseEntity<Obat> updateObat(@PathVariable Long id, @RequestBody Obat obat) {
        return ResponseEntity.ok(pharmacyService.updateObat(id, obat));
    }

    @DeleteMapping("/obat/{id}")
    @Operation(summary = "Soft Delete Item Obat")
    public ResponseEntity<Map<String, String>> deleteObat(@PathVariable Long id) {
        pharmacyService.deleteObat(id);
        return ResponseEntity.ok(Map.of("message", "Obat berhasil dihapus (soft delete)."));
    }

    @GetMapping("/obat/low-stock")
    @Operation(summary = "Get Warning Notifikasi Obat Stok Kritis (stok <= minStok)")
    public ResponseEntity<List<Obat>> getLowStockItems() {
        return ResponseEntity.ok(pharmacyService.getLowStockItems());
    }

    // --- E-Resep ---
    @PostMapping("/resep")
    @Operation(summary = "Buat E-Resep Digital Baru")
    public ResponseEntity<Resep> createResep(@Valid @RequestBody ResepCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.createResep(request));
    }

    @GetMapping("/resep/pasien/{pasienId}")
    @Operation(summary = "Get List Resep milik Pasien")
    public ResponseEntity<List<Resep>> getResepPasien(@PathVariable Long pasienId) {
        return ResponseEntity.ok(pharmacyService.getResepByPasien(pasienId));
    }

    @GetMapping("/resep/queue")
    @Operation(summary = "Get Antrian Penebusan E-Resep Apoteker")
    public ResponseEntity<Page<Resep>> getResepQueue(
            @RequestParam(defaultValue = "PENDING") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(pharmacyService.getResepQueue(status, page, limit));
    }

    @PostMapping("/resep/{id}/fulfill")
    @Operation(summary = "Proses Penebusan Resep & Potong Stok Otomatis")
    public ResponseEntity<Resep> fulfillResep(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.fulfillResep(id));
    }

    // --- Transaksi Kasir POS ---
    @PostMapping("/transaksi/checkout")
    @Operation(summary = "Checkout Pembayaran POS Kasir Apotek & Potong Stok")
    public ResponseEntity<Transaksi> checkout(@Valid @RequestBody TransaksiCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.checkoutTransaksi(request));
    }

    @GetMapping("/transaksi/{invoice}")
    @Operation(summary = "Get Invoice Struk Pembayaran Kasir by Invoice Number")
    public ResponseEntity<Transaksi> getTransaksiByInvoice(@PathVariable String invoice) {
        return ResponseEntity.ok(pharmacyService.getTransaksiByInvoice(invoice));
    }
}
