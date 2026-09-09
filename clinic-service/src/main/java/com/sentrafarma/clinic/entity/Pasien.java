package com.sentrafarma.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pasien")
public class Pasien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nik;

    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;

    @Column(nullable = false)
    private String email;

    @Column(name = "no_telepon")
    private String noTelepon;

    @Column(name = "tanggal_lahir")
    private String tanggalLahir;

    private String alamat;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    @Column(name = "golongan_darah")
    private String golonganDarah;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Pasien() {}

    public Pasien(Long id, Long userId, String nik, String namaLengkap, String email, String noTelepon, String tanggalLahir, String alamat, String jenisKelamin, String golonganDarah, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.nik = nik;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.noTelepon = noTelepon;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.jenisKelamin = jenisKelamin;
        this.golonganDarah = golonganDarah;
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
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    public String getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public String getGolonganDarah() { return golonganDarah; }
    public void setGolonganDarah(String golonganDarah) { this.golonganDarah = golonganDarah; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static PasienBuilder builder() { return new PasienBuilder(); }

    public static class PasienBuilder {
        private Long id;
        private Long userId;
        private String nik;
        private String namaLengkap;
        private String email;
        private String noTelepon;
        private String tanggalLahir;
        private String alamat;
        private String jenisKelamin;
        private String golonganDarah;

        public PasienBuilder id(Long id) { this.id = id; return this; }
        public PasienBuilder userId(Long userId) { this.userId = userId; return this; }
        public PasienBuilder nik(String nik) { this.nik = nik; return this; }
        public PasienBuilder namaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; return this; }
        public PasienBuilder email(String email) { this.email = email; return this; }
        public PasienBuilder noTelepon(String noTelepon) { this.noTelepon = noTelepon; return this; }
        public PasienBuilder tanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; return this; }
        public PasienBuilder alamat(String alamat) { this.alamat = alamat; return this; }
        public PasienBuilder jenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; return this; }
        public PasienBuilder golonganDarah(String golonganDarah) { this.golonganDarah = golonganDarah; return this; }

        public Pasien build() {
            return new Pasien(id, userId, nik, namaLengkap, email, noTelepon, tanggalLahir, alamat, jenisKelamin, golonganDarah, null, null);
        }
    }
}
