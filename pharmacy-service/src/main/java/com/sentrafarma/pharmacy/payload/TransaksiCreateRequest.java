package com.sentrafarma.pharmacy.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class TransaksiCreateRequest {

    private Long resepId;
    private Long pasienId;

    private Long kasirId;

    private String metodePembayaran = "CASH";

    @NotEmpty(message = "Transaksi harus berisi minimal 1 item obat")
    private List<TransaksiItemRequest> items;

    public TransaksiCreateRequest() {}

    public Long getResepId() { return resepId; }
    public void setResepId(Long resepId) { this.resepId = resepId; }
    public Long getPasienId() { return pasienId; }
    public void setPasienId(Long pasienId) { this.pasienId = pasienId; }
    public Long getKasirId() { return kasirId; }
    public void setKasirId(Long kasirId) { this.kasirId = kasirId; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public List<TransaksiItemRequest> getItems() { return items; }
    public void setItems(List<TransaksiItemRequest> items) { this.items = items; }

    public static class TransaksiItemRequest {
        @NotNull(message = "Obat ID wajib diisi")
        private Long obatId;

        @NotNull(message = "Jumlah obat wajib diisi")
        private Integer jumlah;

        public TransaksiItemRequest() {}

        public Long getObatId() { return obatId; }
        public void setObatId(Long obatId) { this.obatId = obatId; }
        public Integer getJumlah() { return jumlah; }
        public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }
    }
}
