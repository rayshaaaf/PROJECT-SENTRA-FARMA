package com.sentrafarma.pharmacy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resep")
public class Resep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rekam_medis_id")
    private Long rekamMedisId;

    @Column(name = "pasien_id", nullable = false)
    private Long pasienId;

    @Column(name = "dokter_id", nullable = false)
    private Long dokterId;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "is_chat_prescription", nullable = false)
    private Boolean isChatPrescription = false;

    @OneToMany(mappedBy = "resep", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetailResep> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Resep() {}

    public Resep(Long id, Long rekamMedisId, Long pasienId, Long dokterId, String status, Boolean isChatPrescription, List<DetailResep> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.rekamMedisId = rekamMedisId;
        this.pasienId = pasienId;
        this.dokterId = dokterId;
        this.status = status != null ? status : "PENDING";
        this.isChatPrescription = isChatPrescription != null ? isChatPrescription : false;
        this.items = items != null ? items : new ArrayList<>();
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
    public Long getRekamMedisId() { return rekamMedisId; }
    public void setRekamMedisId(Long rekamMedisId) { this.rekamMedisId = rekamMedisId; }
    public Long getPasienId() { return pasienId; }
    public void setPasienId(Long pasienId) { this.pasienId = pasienId; }
    public Long getDokterId() { return dokterId; }
    public void setDokterId(Long dokterId) { this.dokterId = dokterId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getIsChatPrescription() { return isChatPrescription; }
    public void setIsChatPrescription(Boolean isChatPrescription) { this.isChatPrescription = isChatPrescription; }
    public List<DetailResep> getItems() { return items; }
    public void setItems(List<DetailResep> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ResepBuilder builder() { return new ResepBuilder(); }

    public static class ResepBuilder {
        private Long id;
        private Long rekamMedisId;
        private Long pasienId;
        private Long dokterId;
        private String status = "PENDING";
        private Boolean isChatPrescription = false;
        private List<DetailResep> items = new ArrayList<>();

        public ResepBuilder id(Long id) { this.id = id; return this; }
        public ResepBuilder rekamMedisId(Long rmId) { this.rekamMedisId = rmId; return this; }
        public ResepBuilder pasienId(Long pasienId) { this.pasienId = pasienId; return this; }
        public ResepBuilder dokterId(Long dokterId) { this.dokterId = dokterId; return this; }
        public ResepBuilder status(String status) { this.status = status; return this; }
        public ResepBuilder isChatPrescription(Boolean isChat) { this.isChatPrescription = isChat; return this; }
        public ResepBuilder items(List<DetailResep> items) { this.items = items; return this; }

        public Resep build() {
            return new Resep(id, rekamMedisId, pasienId, dokterId, status, isChatPrescription, items, null, null);
        }
    }
}
