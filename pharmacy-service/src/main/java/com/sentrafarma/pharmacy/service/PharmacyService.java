package com.sentrafarma.pharmacy.service;

import com.sentrafarma.pharmacy.entity.*;
import com.sentrafarma.pharmacy.payload.ResepCreateRequest;
import com.sentrafarma.pharmacy.payload.TransaksiCreateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PharmacyService {

    // Kategori Obat
    List<KategoriObat> getAllKategori();
    KategoriObat createKategori(KategoriObat kat);

    // Inventory Obat
    Page<Obat> getObatList(int page, int limit, String search, Long kategoriId, String sortBy, String sortDir);
    Obat getObatById(Long id);
    Obat saveObat(Obat obat);
    Obat updateObat(Long id, Obat updated);
    void deleteObat(Long id);
    List<Obat> getLowStockItems();

    // E-Resep
    Resep createResep(ResepCreateRequest req);
    List<Resep> getResepByPasien(Long pasienId);
    Page<Resep> getResepQueue(String status, int page, int limit);
    Resep fulfillResep(Long resepId);

    // Transaksi Kasir POS
    Transaksi checkoutTransaksi(TransaksiCreateRequest req);
    Transaksi getTransaksiByInvoice(String invoice);

    // Reset History & Transactions
    void resetHistory();
}
