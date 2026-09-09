package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "antrian")
public class Antrian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomor_antrian", nullable = false)
    private String nomorAntrian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pasien_id", nullable = false)
    private Pasien pasien;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dokter_id", nullable = false)
    private Dokter dokter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poliklinik_id", nullable = false)
    private Poliklinik poliklinik;

    @Column(name = "tanggal_berobat", nullable = false)
    private String tanggalBerobat;

    @Column(nullable = false)
    private String status = "MENUNGGU";

    @Column(nullable = false)
    private String tipe = "ONLINE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Antrian() {}

    public Antrian(Long id, String nomorAntrian, Pasien pasien, Dokter dokter, Poliklinik poliklinik, String tanggalBerobat, String status, String tipe, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nomorAntrian = nomorAntrian;
        this.pasien = pasien;
        this.dokter = dokter;
        this.poliklinik = poliklinik;
        this.tanggalBerobat = tanggalBerobat;
        this.status = status != null ? status : "MENUNGGU";
        this.tipe = tipe != null ? tipe : "ONLINE";
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
    public String getNomorAntrian() { return nomorAntrian; }
    public void setNomorAntrian(String nomorAntrian) { this.nomorAntrian = nomorAntrian; }
    public Pasien getPasien() { return pasien; }
    public void setPasien(Pasien pasien) { this.pasien = pasien; }
    public Dokter getDokter() { return dokter; }
    public void setDokter(Dokter dokter) { this.dokter = dokter; }
    public Poliklinik getPoliklinik() { return poliklinik; }
    public void setPoliklinik(Poliklinik poliklinik) { this.poliklinik = poliklinik; }
    public String getTanggalBerobat() { return tanggalBerobat; }
    public void setTanggalBerobat(String tanggalBerobat) { this.tanggalBerobat = tanggalBerobat; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static AntrianBuilder builder() { return new AntrianBuilder(); }

    public static class AntrianBuilder {
        private Long id;
        private String nomorAntrian;
        private Pasien pasien;
        private Dokter dokter;
        private Poliklinik poliklinik;
        private String tanggalBerobat;
        private String status = "MENUNGGU";
        private String tipe = "ONLINE";

        public AntrianBuilder id(Long id) { this.id = id; return this; }
        public AntrianBuilder nomorAntrian(String nomorAntrian) { this.nomorAntrian = nomorAntrian; return this; }
        public AntrianBuilder pasien(Pasien pasien) { this.pasien = pasien; return this; }
        public AntrianBuilder dokter(Dokter dokter) { this.dokter = dokter; return this; }
        public AntrianBuilder poliklinik(Poliklinik poliklinik) { this.poliklinik = poliklinik; return this; }
        public AntrianBuilder tanggalBerobat(String tanggalBerobat) { this.tanggalBerobat = tanggalBerobat; return this; }
        public AntrianBuilder status(String status) { this.status = status; return this; }
        public AntrianBuilder tipe(String tipe) { this.tipe = tipe; return this; }

        public Antrian build() {
            return new Antrian(id, nomorAntrian, pasien, dokter, poliklinik, tanggalBerobat, status, tipe, null, null);
        }
    }
}
