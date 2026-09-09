package com.sentrafarma.pharmacy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "obat")
public class Obat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id", nullable = false)
    private KategoriObat kategori;

    @Column(name = "kode_obat", nullable = false, unique = true)
    private String kodeObat;

    @Column(name = "nama_obat", nullable = false)
    private String namaObat;

    @Column(nullable = false)
    private String satuan;

    @Column(nullable = false)
    private Double harga;

    @Column(nullable = false)
    private Integer stok;

    @Column(name = "min_stok", nullable = false)
    private Integer minStok;

    @Column(name = "tanggal_kadaluarsa")
    private String tanggalKadaluarsa;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Obat() {}

    public Obat(Long id, KategoriObat kategori, String kodeObat, String namaObat, String satuan, Double harga, Integer stok, Integer minStok, String tanggalKadaluarsa, String fotoUrl, Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.kategori = kategori;
        this.kodeObat = kodeObat;
        this.namaObat = namaObat;
        this.satuan = satuan;
        this.harga = harga;
        this.stok = stok;
        this.minStok = minStok;
        this.tanggalKadaluarsa = tanggalKadaluarsa;
        this.fotoUrl = fotoUrl;
        this.isDeleted = isDeleted != null ? isDeleted : false;
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
    public KategoriObat getKategori() { return kategori; }
    public void setKategori(KategoriObat kategori) { this.kategori = kategori; }
    public String getKodeObat() { return kodeObat; }
    public void setKodeObat(String kodeObat) { this.kodeObat = kodeObat; }
    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public Double getHarga() { return harga; }
    public void setHarga(Double harga) { this.harga = harga; }
    public Integer getStok() { return stok; }
    public void setStok(Integer stok) { this.stok = stok; }
    public Integer getMinStok() { return minStok; }
    public void setMinStok(Integer minStok) { this.minStok = minStok; }
    public String getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(String tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ObatBuilder builder() { return new ObatBuilder(); }

    public static class ObatBuilder {
        private Long id;
        private KategoriObat kategori;
        private String kodeObat;
        private String namaObat;
        private String satuan;
        private Double harga;
        private Integer stok;
        private Integer minStok;
        private String tanggalKadaluarsa;
        private String fotoUrl;
        private Boolean isDeleted = false;

        public ObatBuilder id(Long id) { this.id = id; return this; }
        public ObatBuilder kategori(KategoriObat kategori) { this.kategori = kategori; return this; }
        public ObatBuilder kodeObat(String kodeObat) { this.kodeObat = kodeObat; return this; }
        public ObatBuilder namaObat(String namaObat) { this.namaObat = namaObat; return this; }
        public ObatBuilder satuan(String satuan) { this.satuan = satuan; return this; }
        public ObatBuilder harga(Double harga) { this.harga = harga; return this; }
        public ObatBuilder stok(Integer stok) { this.stok = stok; return this; }
        public ObatBuilder minStok(Integer minStok) { this.minStok = minStok; return this; }
        public ObatBuilder tanggalKadaluarsa(String tgl) { this.tanggalKadaluarsa = tgl; return this; }
        public ObatBuilder fotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; return this; }
        public ObatBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Obat build() {
            return new Obat(id, kategori, kodeObat, namaObat, satuan, harga, stok, minStok, tanggalKadaluarsa, fotoUrl, isDeleted, null, null);
        }
    }
}
