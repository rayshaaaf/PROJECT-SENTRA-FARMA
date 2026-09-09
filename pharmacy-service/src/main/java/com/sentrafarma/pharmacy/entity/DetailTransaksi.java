package com.sentrafarma.pharmacy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "detail_transaksi")
public class DetailTransaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaksi_id", nullable = false)
    @JsonIgnore
    private Transaksi transaksi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "obat_id", nullable = false)
    private Obat obat;

    @Column(nullable = false)
    private Integer jumlah;

    @Column(name = "harga_satuan", nullable = false)
    private Double hargaSatuan;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    public DetailTransaksi() {}

    public DetailTransaksi(Long id, Transaksi transaksi, Obat obat, Integer jumlah, Double hargaSatuan, Double subtotal) {
        this.id = id;
        this.transaksi = transaksi;
        this.obat = obat;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = subtotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }
    public Obat getObat() { return obat; }
    public void setObat(Obat obat) { this.obat = obat; }
    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }
    public Double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(Double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public static DetailTransaksiBuilder builder() { return new DetailTransaksiBuilder(); }

    public static class DetailTransaksiBuilder {
        private Long id;
        private Transaksi transaksi;
        private Obat obat;
        private Integer jumlah;
        private Double hargaSatuan;
        private Double subtotal;

        public DetailTransaksiBuilder id(Long id) { this.id = id; return this; }
        public DetailTransaksiBuilder transaksi(Transaksi t) { this.transaksi = t; return this; }
        public DetailTransaksiBuilder obat(Obat obat) { this.obat = obat; return this; }
        public DetailTransaksiBuilder jumlah(Integer jumlah) { this.jumlah = jumlah; return this; }
        public DetailTransaksiBuilder hargaSatuan(Double h) { this.hargaSatuan = h; return this; }
        public DetailTransaksiBuilder subtotal(Double subtotal) { this.subtotal = subtotal; return this; }

        public DetailTransaksi build() {
            return new DetailTransaksi(id, transaksi, obat, jumlah, hargaSatuan, subtotal);
        }
    }
}
