package com.sentrafarma.pharmacy.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ResepCreateRequest {

    private Long rekamMedisId;

    @NotNull(message = "Pasien ID wajib diisi")
    private Long pasienId;

    @NotNull(message = "Dokter ID wajib diisi")
    private Long dokterId;

    private Boolean isChatPrescription = false;

    @NotEmpty(message = "Resep harus berisi minimal 1 item obat")
    private List<ResepItemRequest> items;

    public ResepCreateRequest() {}

    public Long getRekamMedisId() { return rekamMedisId; }
    public void setRekamMedisId(Long rekamMedisId) { this.rekamMedisId = rekamMedisId; }
    public Long getPasienId() { return pasienId; }
    public void setPasienId(Long pasienId) { this.pasienId = pasienId; }
    public Long getDokterId() { return dokterId; }
    public void setDokterId(Long dokterId) { this.dokterId = dokterId; }
    public Boolean getIsChatPrescription() { return isChatPrescription; }
    public void setIsChatPrescription(Boolean isChatPrescription) { this.isChatPrescription = isChatPrescription; }
    public List<ResepItemRequest> getItems() { return items; }
    public void setItems(List<ResepItemRequest> items) { this.items = items; }

    public static class ResepItemRequest {
        @NotNull(message = "Obat ID wajib diisi")
        private Long obatId;

        @NotNull(message = "Jumlah obat wajib diisi")
        private Integer jumlah;

        private String aturanPakai;

        public ResepItemRequest() {}

        public Long getObatId() { return obatId; }
        public void setObatId(Long obatId) { this.obatId = obatId; }
        public Integer getJumlah() { return jumlah; }
        public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }
        public String getAturanPakai() { return aturanPakai; }
        public void setAturanPakai(String aturanPakai) { this.aturanPakai = aturanPakai; }
    }
}
