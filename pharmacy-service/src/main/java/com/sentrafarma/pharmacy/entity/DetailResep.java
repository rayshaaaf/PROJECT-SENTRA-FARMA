package com.sentrafarma.pharmacy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "detail_resep")
public class DetailResep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resep_id", nullable = false)
    @JsonIgnore
    private Resep resep;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "obat_id", nullable = false)
    private Obat obat;

    @Column(nullable = false)
    private Integer jumlah;

    @Column(nullable = false)
    private String aturanPakai;

    public DetailResep() {}

    public DetailResep(Long id, Resep resep, Obat obat, Integer jumlah, String aturanPakai) {
        this.id = id;
        this.resep = resep;
        this.obat = obat;
        this.jumlah = jumlah;
        this.aturanPakai = aturanPakai;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Resep getResep() { return resep; }
    public void setResep(Resep resep) { this.resep = resep; }
    public Obat getObat() { return obat; }
    public void setObat(Obat obat) { this.obat = obat; }
    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }
    public String getAturanPakai() { return aturanPakai; }
    public void setAturanPakai(String aturanPakai) { this.aturanPakai = aturanPakai; }

    public static DetailResepBuilder builder() { return new DetailResepBuilder(); }

    public static class DetailResepBuilder {
        private Long id;
        private Resep resep;
        private Obat obat;
        private Integer jumlah;
        private String aturanPakai;

        public DetailResepBuilder id(Long id) { this.id = id; return this; }
        public DetailResepBuilder resep(Resep resep) { this.resep = resep; return this; }
        public DetailResepBuilder obat(Obat obat) { this.obat = obat; return this; }
        public DetailResepBuilder jumlah(Integer jumlah) { this.jumlah = jumlah; return this; }
        public DetailResepBuilder aturanPakai(String aturanPakai) { this.aturanPakai = aturanPakai; return this; }

        public DetailResep build() {
            return new DetailResep(id, resep, obat, jumlah, aturanPakai);
        }
    }
}
