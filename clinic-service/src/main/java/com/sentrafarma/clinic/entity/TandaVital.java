package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tanda_vital")
public class TandaVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rekam_medis_id", nullable = false)
    private RekamMedis rekamMedis;

    @Column(name = "tekanan_darah_sistol")
    private Integer tekananDarahSistol;

    @Column(name = "tekanan_darah_diastol")
    private Integer tekananDarahDiastol;

    @Column(name = "detak_jantung")
    private Integer detakJantung;

    @Column(name = "suhu_tubuh")
    private Double suhuTubuh;

    @Column(name = "berat_badan")
    private Double beratBadan;

    @Column(name = "tinggi_tubuh")
    private Double tinggiTubuh;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TandaVital() {}

    public TandaVital(Long id, RekamMedis rekamMedis, Integer tekananDarahSistol, Integer tekananDarahDiastol, Integer detakJantung, Double suhuTubuh, Double beratBadan, Double tinggiTubuh, LocalDateTime createdAt) {
        this.id = id;
        this.rekamMedis = rekamMedis;
        this.tekananDarahSistol = tekananDarahSistol;
        this.tekananDarahDiastol = tekananDarahDiastol;
        this.detakJantung = detakJantung;
        this.suhuTubuh = suhuTubuh;
        this.beratBadan = beratBadan;
        this.tinggiTubuh = tinggiTubuh;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RekamMedis getRekamMedis() { return rekamMedis; }
    public void setRekamMedis(RekamMedis rekamMedis) { this.rekamMedis = rekamMedis; }
    public Integer getTekananDarahSistol() { return tekananDarahSistol; }
    public void setTekananDarahSistol(Integer tekananDarahSistol) { this.tekananDarahSistol = tekananDarahSistol; }
    public Integer getTekananDarahDiastol() { return tekananDarahDiastol; }
    public void setTekananDarahDiastol(Integer tekananDarahDiastol) { this.tekananDarahDiastol = tekananDarahDiastol; }
    public Integer getDetakJantung() { return detakJantung; }
    public void setDetakJantung(Integer detakJantung) { this.detakJantung = detakJantung; }
    public Double getSuhuTubuh() { return suhuTubuh; }
    public void setSuhuTubuh(Double suhuTubuh) { this.suhuTubuh = suhuTubuh; }
    public Double getBeratBadan() { return beratBadan; }
    public void setBeratBadan(Double beratBadan) { this.beratBadan = beratBadan; }
    public Double getTinggiTubuh() { return tinggiTubuh; }
    public void setTinggiTubuh(Double tinggiTubuh) { this.tinggiTubuh = tinggiTubuh; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static TandaVitalBuilder builder() { return new TandaVitalBuilder(); }

    public static class TandaVitalBuilder {
        private Long id;
        private RekamMedis rekamMedis;
        private Integer tekananDarahSistol;
        private Integer tekananDarahDiastol;
        private Integer detakJantung;
        private Double suhuTubuh;
        private Double beratBadan;
        private Double tinggiTubuh;

        public TandaVitalBuilder id(Long id) { this.id = id; return this; }
        public TandaVitalBuilder rekamMedis(RekamMedis rekamMedis) { this.rekamMedis = rekamMedis; return this; }
        public TandaVitalBuilder tekananDarahSistol(Integer s) { this.tekananDarahSistol = s; return this; }
        public TandaVitalBuilder tekananDarahDiastol(Integer d) { this.tekananDarahDiastol = d; return this; }
        public TandaVitalBuilder detakJantung(Integer dj) { this.detakJantung = dj; return this; }
        public TandaVitalBuilder suhuTubuh(Double st) { this.suhuTubuh = st; return this; }
        public TandaVitalBuilder beratBadan(Double bb) { this.beratBadan = bb; return this; }
        public TandaVitalBuilder tinggiTubuh(Double tt) { this.tinggiTubuh = tt; return this; }

        public TandaVital build() {
            return new TandaVital(id, rekamMedis, tekananDarahSistol, tekananDarahDiastol, detakJantung, suhuTubuh, beratBadan, tinggiTubuh, null);
        }
    }
}
