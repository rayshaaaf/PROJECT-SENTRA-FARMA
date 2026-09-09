package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "poliklinik")
public class Poliklinik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_poli", nullable = false)
    private String namaPoli;

    private String deskripsi;
    private String ruangan;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Poliklinik() {}

    public Poliklinik(Long id, String namaPoli, String deskripsi, String ruangan, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.namaPoli = namaPoli;
        this.deskripsi = deskripsi;
        this.ruangan = ruangan;
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
    public String getNamaPoli() { return namaPoli; }
    public void setNamaPoli(String namaPoli) { this.namaPoli = namaPoli; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getRuangan() { return ruangan; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static PoliklinikBuilder builder() { return new PoliklinikBuilder(); }

    public static class PoliklinikBuilder {
        private Long id;
        private String namaPoli;
        private String deskripsi;
        private String ruangan;

        public PoliklinikBuilder id(Long id) { this.id = id; return this; }
        public PoliklinikBuilder namaPoli(String namaPoli) { this.namaPoli = namaPoli; return this; }
        public PoliklinikBuilder deskripsi(String deskripsi) { this.deskripsi = deskripsi; return this; }
        public PoliklinikBuilder ruangan(String ruangan) { this.ruangan = ruangan; return this; }

        public Poliklinik build() {
            return new Poliklinik(id, namaPoli, deskripsi, ruangan, null, null);
        }
    }
}
