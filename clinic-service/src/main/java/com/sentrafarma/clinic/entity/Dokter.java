package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dokter")
public class Dokter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nama_dokter", nullable = false)
    private String namaDokter;

    @Column(nullable = false, unique = true)
    private String sip;

    @Column(nullable = false)
    private String spesialisasi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poliklinik_id")
    private Poliklinik poliklinik;

    @Column(name = "status_duty", nullable = false)
    private String statusDuty = "ON_DUTY";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Dokter() {}

    public Dokter(Long id, Long userId, String namaDokter, String sip, String spesialisasi, Poliklinik poliklinik, String statusDuty, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.namaDokter = namaDokter;
        this.sip = sip;
        this.spesialisasi = spesialisasi;
        this.poliklinik = poliklinik;
        this.statusDuty = statusDuty != null ? statusDuty : "ON_DUTY";
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
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNamaDokter() { return namaDokter; }
    public void setNamaDokter(String namaDokter) { this.namaDokter = namaDokter; }
    public String getSip() { return sip; }
    public void setSip(String sip) { this.sip = sip; }
    public String getSpesialisasi() { return spesialisasi; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    public Poliklinik getPoliklinik() { return poliklinik; }
    public void setPoliklinik(Poliklinik poliklinik) { this.poliklinik = poliklinik; }
    public String getStatusDuty() { return statusDuty; }
    public void setStatusDuty(String statusDuty) { this.statusDuty = statusDuty; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static DokterBuilder builder() { return new DokterBuilder(); }

    public static class DokterBuilder {
        private Long id;
        private Long userId;
        private String namaDokter;
        private String sip;
        private String spesialisasi;
        private Poliklinik poliklinik;
        private String statusDuty = "ON_DUTY";

        public DokterBuilder id(Long id) { this.id = id; return this; }
        public DokterBuilder userId(Long userId) { this.userId = userId; return this; }
        public DokterBuilder namaDokter(String namaDokter) { this.namaDokter = namaDokter; return this; }
        public DokterBuilder sip(String sip) { this.sip = sip; return this; }
        public DokterBuilder spesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; return this; }
        public DokterBuilder poliklinik(Poliklinik poliklinik) { this.poliklinik = poliklinik; return this; }
        public DokterBuilder statusDuty(String statusDuty) { this.statusDuty = statusDuty; return this; }

        public Dokter build() {
            return new Dokter(id, userId, namaDokter, sip, spesialisasi, poliklinik, statusDuty, null, null);
        }
    }
}
