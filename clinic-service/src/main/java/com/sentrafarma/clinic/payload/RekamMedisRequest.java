package com.sentrafarma.clinic.payload;

import jakarta.validation.constraints.NotNull;

public class RekamMedisRequest {

    private Long antrianId;

    @NotNull(message = "Pasien ID wajib diisi")
    private Long pasienId;

    @NotNull(message = "Dokter ID wajib diisi")
    private Long dokterId;

    private String keluhanUtama;
    private String diagnosaIcd10;
    private String catatanDokter;

    // Vital signs
    private Integer tekananDarahSistol;
    private Integer tekananDarahDiastol;
    private Integer detakJantung;
    private Double suhuTubuh;
    private Double beratBadan;
    private Double tinggiTubuh;

    public RekamMedisRequest() {}

    public Long getAntrianId() { return antrianId; }
    public void setAntrianId(Long antrianId) { this.antrianId = antrianId; }
    public Long getPasienId() { return pasienId; }
    public void setPasienId(Long pasienId) { this.pasienId = pasienId; }
    public Long getDokterId() { return dokterId; }
    public void setDokterId(Long dokterId) { this.dokterId = dokterId; }
    public String getKeluhanUtama() { return keluhanUtama; }
    public void setKeluhanUtama(String keluhanUtama) { this.keluhanUtama = keluhanUtama; }
    public String getDiagnosaIcd10() { return diagnosaIcd10; }
    public void setDiagnosaIcd10(String diagnosaIcd10) { this.diagnosaIcd10 = diagnosaIcd10; }
    public String getCatatanDokter() { return catatanDokter; }
    public void setCatatanDokter(String catatanDokter) { this.catatanDokter = catatanDokter; }
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
}
