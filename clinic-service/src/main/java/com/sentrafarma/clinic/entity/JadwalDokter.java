package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jadwal_dokter")
public class JadwalDokter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dokter_id", nullable = false)
    private Dokter dokter;

    @Column(nullable = false)
    private String hari;

    @Column(name = "jam_mulai", nullable = false)
    private String jamMulai;

    @Column(name = "jam_selesai", nullable = false)
    private String jamSelesai;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public JadwalDokter() {}

    public JadwalDokter(Long id, Dokter dokter, String hari, String jamMulai, String jamSelesai, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dokter = dokter;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
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
    public Dokter getDokter() { return dokter; }
    public void setDokter(Dokter dokter) { this.dokter = dokter; }
    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }
    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }
    public String getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static JadwalDokterBuilder builder() { return new JadwalDokterBuilder(); }

    public static class JadwalDokterBuilder {
        private Long id;
        private Dokter dokter;
        private String hari;
        private String jamMulai;
        private String jamSelesai;

        public JadwalDokterBuilder id(Long id) { this.id = id; return this; }
        public JadwalDokterBuilder dokter(Dokter dokter) { this.dokter = dokter; return this; }
        public JadwalDokterBuilder hari(String hari) { this.hari = hari; return this; }
        public JadwalDokterBuilder jamMulai(String jamMulai) { this.jamMulai = jamMulai; return this; }
        public JadwalDokterBuilder jamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; return this; }

        public JadwalDokter build() {
            return new JadwalDokter(id, dokter, hari, jamMulai, jamSelesai, null, null);
        }
    }
}
