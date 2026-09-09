package com.sentrafarma.pharmacy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomor_invoice", nullable = false, unique = true)
    private String nomorInvoice;

    @Column(name = "resep_id")
    private Long resepId;

    @Column(name = "pasien_id")
    private Long pasienId;

    @Column(name = "kasir_id", nullable = false)
    private Long kasirId;

    @Column(name = "total_harga", nullable = false)
    private Double totalHarga;

    @Column(name = "metode_pembayaran", nullable = false)
    private String metodePembayaran = "CASH";

    @Column(nullable = false)
    private String status = "LUNAS";

    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetailTransaksi> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaksi() {}

    public Transaksi(Long id, String nomorInvoice, Long resepId, Long pasienId, Long kasirId, Double totalHarga, String metodePembayaran, String status, List<DetailTransaksi> items, LocalDateTime createdAt) {
        this.id = id;
        this.nomorInvoice = nomorInvoice;
        this.resepId = resepId;
        this.pasienId = pasienId;
        this.kasirId = kasirId;
        this.totalHarga = totalHarga;
        this.metodePembayaran = metodePembayaran != null ? metodePembayaran : "CASH";
        this.status = status != null ? status : "LUNAS";
        this.items = items != null ? items : new ArrayList<>();
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomorInvoice() { return nomorInvoice; }
    public void setNomorInvoice(String nomorInvoice) { this.nomorInvoice = nomorInvoice; }
    public Long getResepId() { return resepId; }
    public void setResepId(Long resepId) { this.resepId = resepId; }
    public Long getPasienId() { return pasienId; }
    public void setPasienId(Long pasienId) { this.pasienId = pasienId; }
    public Long getKasirId() { return kasirId; }
    public void setKasirId(Long kasirId) { this.kasirId = kasirId; }
    public Double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(Double totalHarga) { this.totalHarga = totalHarga; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<DetailTransaksi> getItems() { return items; }
    public void setItems(List<DetailTransaksi> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static TransaksiBuilder builder() { return new TransaksiBuilder(); }

    public static class TransaksiBuilder {
        private Long id;
        private String nomorInvoice;
        private Long resepId;
        private Long pasienId;
        private Long kasirId;
        private Double totalHarga;
        private String metodePembayaran = "CASH";
        private String status = "LUNAS";
        private List<DetailTransaksi> items = new ArrayList<>();

        public TransaksiBuilder id(Long id) { this.id = id; return this; }
        public TransaksiBuilder nomorInvoice(String nomorInvoice) { this.nomorInvoice = nomorInvoice; return this; }
        public TransaksiBuilder resepId(Long resepId) { this.resepId = resepId; return this; }
        public TransaksiBuilder pasienId(Long pasienId) { this.pasienId = pasienId; return this; }
        public TransaksiBuilder kasirId(Long kasirId) { this.kasirId = kasirId; return this; }
        public TransaksiBuilder totalHarga(Double totalHarga) { this.totalHarga = totalHarga; return this; }
        public TransaksiBuilder metodePembayaran(String m) { this.metodePembayaran = m; return this; }
        public TransaksiBuilder status(String status) { this.status = status; return this; }
        public TransaksiBuilder items(List<DetailTransaksi> items) { this.items = items; return this; }

        public Transaksi build() {
            return new Transaksi(id, nomorInvoice, resepId, pasienId, kasirId, totalHarga, metodePembayaran, status, items, null);
        }
    }
}
