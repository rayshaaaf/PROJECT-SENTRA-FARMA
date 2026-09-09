# Dokumentasi Perancangan Sistem: Sentra Farma

## 1. Flowchart Otentikasi & Autorisasi Pengguna (JWT & RBAC)

```mermaid
flowchart TD
    Start([Mulai]) --> FormLogin[Pengguna Membuka Halaman Login / Register]
    FormLogin --> Submit[Submit Email & Password]
    Submit --> AuthValidation{Validasi Format & Hash BCrypt}
    AuthValidation -- Tidak Valid --> ErrorToast[Tampilkan Toast Notification Error 401/422]
    ErrorToast --> FormLogin
    AuthValidation -- Valid --> GenJWT[Auth Service Terbitkan Token JWT Access]
    GenJWT --> SaveStorage[Simpan JWT & User Data di LocalStorage Client]
    SaveStorage --> CheckRole{Cek Role Pengguna}
    CheckRole -- PASIEN --> DashPasien[Arahkan ke Portal Pasien]
    CheckRole -- DOKTER --> DashDokter[Arahkan ke Portal Dokter]
    CheckRole -- APOTEKER --> DashApotek[Arahkan ke Portal Apoteker & Kasir]
    CheckRole -- RESEPSIONIS --> DashResepsionis[Arahkan ke Portal Resepsionis]
    CheckRole -- ADMIN --> DashAdmin[Arahkan ke Master Control Admin]
    DashPasien --> Selesai([Selesai])
    DashDokter --> Selesai
    DashApotek --> Selesai
    DashResepsionis --> Selesai
    DashAdmin --> Selesai
```

---

## 2. Flowchart Alur Berobat Pasien & Antrian Klinik Digital

```mermaid
flowchart TD
    Start([Pasien Mendaftar Berobat]) --> ChooseType{Tipe Pendaftaran}
    ChooseType -- Online --> PortalOnline[Pasien Pilih Poli & Dokter di Website]
    ChooseType -- Offline Walk-in --> Resepsionis[Resepsionis Input Pendaftaran Pasien Datang Langsung]
    PortalOnline --> GenQueue[System Terbitkan Nomor Antrian Digital e.g. C-05]
    Resepsionis --> GenQueue
    GenQueue --> SaveAntrian[Tersimpan di Table antrian PostgreSQL]
    SaveAntrian --> DisplayQueue[Tampil di Live Queue Tracker & Screen TV Ruang Tunggu]
    DisplayQueue --> CallDoctor[Dokter/Resepsionis Panggil Antrian C-05]
    CallDoctor --> Exam[Dokter Melakukan Pemeriksaan Medis]
    Exam --> InputRm[Dokter Input Diagnosa ICD-10 & Tanda Vital SISTOL/DIASTOL/SUHU/BB]
    InputRm --> GenRecipe[Dokter Buat E-Resep Obat Digital]
    GenRecipe --> SendPharmacy[Resep Terkirim Otomatis ke Pharmacy Service]
    SendPharmacy --> ApotekerProcess[Apoteker Menyiapkan Obat & Penebusan]
    ApotekerProcess --> ReduceStock[Stok Obat Berkurang Otomatis di Database]
    ReduceStock --> UpdateDashboard[Dashboard Portal Pasien Ter-update Otomatis]
    UpdateDashboard --> Selesai([Selesai])
```

---

## 3. Flowchart Konsultasi Live Chat Dokter & Tebus Resep ("Halodoc Style")

```mermaid
flowchart TD
    Start([Pasien Buka Menu Live Chat]) --> SelectDoctor[Pilih Dokter Status ON_DUTY]
    SelectDoctor --> InitChat[Buka Ruang Sesi Obrolan Realtime]
    InitChat --> ChatSession[Pasien & Dokter Saling Berkirim Pesan Teks Medis]
    ChatSession --> DoctorPrescribe[Dokter Tekan Button '+ Buat Resep Chat']
    DoctorPrescribe --> CardPrescription[Kartu E-Resep Digital Muncul di Sesi Chat]
    CardPrescription --> PatientClick[Pasien Klik 'Tebus Obat & Pick-up']
    PatientClick --> SendApotek[Order Terkirim ke Apotek Sentra Farma]
    SendApotek --> ApotekerFulfill[Apoteker Konfirmasi & Potong Stok]
    ApotekerFulfill --> Selesai([Selesai])
```

---

## 4. Flowchart Penjualan Obat Bebas Kasir Apotek (POS & Catalogue)

```mermaid
flowchart TD
    Start([Pembelian Obat Bebas]) --> BrowseCatalog[Buka Katalog / Transaksi Kasir]
    BrowseCatalog --> SelectMedicine[Pilih Obat & Jumlah Pembelian]
    SelectMedicine --> CheckStock{Stok Mencukupi?}
    CheckStock -- Stok Tidak Cukup --> AlertStock[Notifikasi Stok Kritis / Tidak Mencukupi]
    AlertStock --> BrowseCatalog
    CheckStock -- Cukup --> AddCart[Tambahkan ke Shopping Cart / Kasir POS]
    AddCart --> Checkout[Proses Checkout Pembayaran CASH/QRIS/TRANSFER]
    Checkout --> GenInvoice[System Terbitkan Invoice e.g. INV-20260830-001]
    GenInvoice --> DeductStock[Otomatis Potong Stok di Table obat PostgreSQL]
    DeductStock --> PrintReceipt[Cetak Struk Pembayaran & Serahkan Obat]
    PrintReceipt --> Selesai([Selesai])
```
