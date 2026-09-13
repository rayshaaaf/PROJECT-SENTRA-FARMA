package com.sentrafarma.clinic.payload;

import jakarta.validation.constraints.NotNull;

public class AntrianRequest {

    @NotNull(message = "Pasien ID wajib diisi")
    private Long pasienId;

    @NotNull(message = "Dokter ID wajib diisi")
    private Long dokterId;

    @NotNull(message = "Poliklinik ID wajib diisi")
    private Long poliklinikId;

    private String tanggalBerobat;

    private String tipe = "ONLINE";

    public AntrianRequest() {}

    public Long getPasienId() { return pasienId; }
    public void setPasienId(Long pasienId) { this.pasienId = pasienId; }
    public Long getDokterId() { return dokterId; }
    public void setDokterId(Long dokterId) { this.dokterId = dokterId; }
    public Long getPoliklinikId() { return poliklinikId; }
    public void setPoliklinikId(Long poliklinikId) { this.poliklinikId = poliklinikId; }
    public String getTanggalBerobat() { return tanggalBerobat; }
    public void setTanggalBerobat(String tanggalBerobat) { this.tanggalBerobat = tanggalBerobat; }
    public String getTanggalAntrian() { return tanggalBerobat; }
    public void setTanggalAntrian(String tanggalAntrian) { this.tanggalBerobat = tanggalAntrian; }
    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
}
