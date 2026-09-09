package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rekam_medis")
public class RekamMedis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "antrian_id")
    private Antrian antrian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pasien_id", nullable = false)
    private Pasien pasien;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dokter_id", nullable = false)
    private Dokter dokter;

    @Column(name = "keluhan_utama", columnDefinition = "TEXT")
    private String keluhanUtama;

    @Column(name = "diagnosa_icd10")
    private String diagnosaIcd10;

    @Column(name = "catatan_dokter", columnDefinition = "TEXT")
    private String catatanDokter;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RekamMedis() {}

    public RekamMedis(Long id, Antrian antrian, Pasien pasien, Dokter dokter, String keluhanUtama, String diagnosaIcd10, String catatanDokter, Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.antrian = antrian;
        this.pasien = pasien;
        this.dokter = dokter;
        this.keluhanUtama = keluhanUtama;
        this.diagnosaIcd10 = diagnosaIcd10;
        this.catatanDokter = catatanDokter;
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
    public Antrian getAntrian() { return antrian; }
    public void setAntrian(Antrian antrian) { this.antrian = antrian; }
    public Pasien getPasien() { return pasien; }
    public void setPasien(Pasien pasien) { this.pasien = pasien; }
    public Dokter getDokter() { return dokter; }
    public void setDokter(Dokter dokter) { this.dokter = dokter; }
    public String getKeluhanUtama() { return keluhanUtama; }
    public void setKeluhanUtama(String keluhanUtama) { this.keluhanUtama = keluhanUtama; }
    public String getDiagnosaIcd10() { return diagnosaIcd10; }
    public void setDiagnosaIcd10(String diagnosaIcd10) { this.diagnosaIcd10 = diagnosaIcd10; }
    public String getCatatanDokter() { return catatanDokter; }
    public void setCatatanDokter(String catatanDokter) { this.catatanDokter = catatanDokter; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static RekamMedisBuilder builder() { return new RekamMedisBuilder(); }

    public static class RekamMedisBuilder {
        private Long id;
        private Antrian antrian;
        private Pasien pasien;
        private Dokter dokter;
        private String keluhanUtama;
        private String diagnosaIcd10;
        private String catatanDokter;
        private Boolean isDeleted = false;

        public RekamMedisBuilder id(Long id) { this.id = id; return this; }
        public RekamMedisBuilder antrian(Antrian antrian) { this.antrian = antrian; return this; }
        public RekamMedisBuilder pasien(Pasien pasien) { this.pasien = pasien; return this; }
        public RekamMedisBuilder dokter(Dokter dokter) { this.dokter = dokter; return this; }
        public RekamMedisBuilder keluhanUtama(String keluhanUtama) { this.keluhanUtama = keluhanUtama; return this; }
        public RekamMedisBuilder diagnosaIcd10(String diagnosaIcd10) { this.diagnosaIcd10 = diagnosaIcd10; return this; }
        public RekamMedisBuilder catatanDokter(String catatanDokter) { this.catatanDokter = catatanDokter; return this; }
        public RekamMedisBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public RekamMedis build() {
            return new RekamMedis(id, antrian, pasien, dokter, keluhanUtama, diagnosaIcd10, catatanDokter, isDeleted, null, null);
        }
    }
}
