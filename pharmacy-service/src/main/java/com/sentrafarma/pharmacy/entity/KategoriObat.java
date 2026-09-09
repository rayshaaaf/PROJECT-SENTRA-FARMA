package com.sentrafarma.pharmacy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kategori_obat")
public class KategoriObat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_kategori", nullable = false)
    private String namaKategori;

    private String deskripsi;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public KategoriObat() {}

    public KategoriObat(Long id, String namaKategori, String deskripsi, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static KategoriObatBuilder builder() { return new KategoriObatBuilder(); }

    public static class KategoriObatBuilder {
        private Long id;
        private String namaKategori;
        private String deskripsi;

        public KategoriObatBuilder id(Long id) { this.id = id; return this; }
        public KategoriObatBuilder namaKategori(String namaKategori) { this.namaKategori = namaKategori; return this; }
        public KategoriObatBuilder deskripsi(String deskripsi) { this.deskripsi = deskripsi; return this; }

        public KategoriObat build() {
            return new KategoriObat(id, namaKategori, deskripsi, null, null);
        }
    }
}
