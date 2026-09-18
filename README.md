# Sentra Farma — Sistem Manajemen Klinik & Apotek Terintegrasi

## Deskripsi Aplikasi

**Sentra Farma** adalah platform Sistem Manajemen Klinik dan Apotek Terintegrasi berbasis web yang dirancang menggunakan arsitektur **Microservices Monorepo**. Platform ini menghubungkan alur operasional klinik secara *end-to-end*, mulai dari pendaftaran antrean pasien, konsultasi rekam medis dokter, penerbitan e-resep digital, pengelolaan inventaris apotek, hingga transaksi kasir (Point of Sale) dan manajemen master data oleh Administrator.

---

## Teknologi yang Digunakan

### Backend & Microservices
- **Java 17** & **Spring Boot 3**
- **Spring Cloud Gateway** (API Gateway, Router, & CORS Management)
- **Spring Security** & **JWT (JSON Web Token)** (Otentikasi & Otorisasi RBAC)
- **Spring Data JPA / Hibernate** (Object-Relational Mapping & Persistence)
- **Springdoc OpenAPI / Swagger UI** (Dokumentasi API Interaktif)

### Database
- **PostgreSQL** (Relational Database System)

### Frontend
- **HTML5** & **Vanilla JavaScript** (ES6+)
- **Tailwind CSS** (Styling & Modern UI Framework)

### Tooling & Build System
- **Apache Maven** (Dependency Management & Build Tool)

---

## Fitur Utama

1. **Portal Pasien**
   - Live Queue Tracker (Monitoring status antrean secara real-time).
   - Bento Grid Dashboard (Informasi tanda vital, histori pemeriksaan, & QR Code Profil).
   - Katalog Apotek & Pencarian Obat.
   - Konsultasi / Live Chat Dokter.

2. **Portal Dokter**
   - Manajemen Queue Antrean Poliklinik.
   - Input Rekam Medis & Tanda Vital Pasien (Diagnosis berbasis standar ICD-10).
   - Penerbitan E-Resep Digital terintegrasi langsung dengan modul Apotek.
   - Chat Konsultasi Pasien.

3. **Portal Apoteker & Kasir (POS)**
   - Penebusan E-Resep & pemotongan stok obat secara otomatis.
   - Kasir / Point of Sale (POS) untuk transaksi penjualan obat bebas.
   - Inventory Management (Pengelolaan stok obat, alert stok menipis, & soft delete data).

4. **Portal Resepsionis**
   - Pendaftaran Pasien Walk-in (Pendaftaran offline).
   - Pemanggilan & pembaruan status antrean pasien.
   - Pengelolaan jadwal dokter jaga.

5. **Portal Administrator (Admin)**
   - Master Data Management (CRUD Pengguna, Dokter, Pasien, Obat, & Poliklinik).
   - Multi-Role Based Access Control (RBAC 5 Role).
   - Analytics Dashboard & Export Laporan (PDF/Excel).

---

## Struktur Folder Monorepo

```
PROJECT SENTRA FARMA/
├── api-gateway/          # Port 8080 (Spring Cloud Gateway Entry Point & CORS)
├── auth-service/         # Port 8081 (Authentication, JWT Token, User Management)
│   └── src/main/java/com/sentrafarma/auth/
│       ├── entity/       # User, Role, PasswordResetToken
│       ├── payload/      # RegisterRequest, LoginRequest, AuthResponse, UserDto, etc.
│       ├── repository/
│       ├── service/
│       └── controller/
├── clinic-service/       # Port 8082 (Pasien, Dokter, Antrian, Rekam Medis, Chat)
│   └── src/main/java/com/sentrafarma/clinic/
│       ├── entity/       # Poliklinik, Dokter, Pasien, Antrian, RekamMedis, TandaVital, etc.
│       ├── payload/      # AntrianRequest, RekamMedisRequest, ErrorResponse
│       ├── repository/
│       ├── service/
│       └── controller/
├── pharmacy-service/     # Port 8083 (Inventory Obat, E-Resep, Transaksi Kasir)
│   └── src/main/java/com/sentrafarma/pharmacy/
│       ├── entity/       # KategoriObat, Obat, Resep, DetailResep, Transaksi, DetailTransaksi
│       ├── payload/      # ResepCreateRequest, TransaksiCreateRequest, ErrorResponse
│       ├── repository/
│       ├── service/
│       └── controller/
├── frontend/             # Front-end UI (HTML5, Tailwind CSS, Vanilla JS)
│   ├── html/
│   │   ├── auth/         # Login, Register, Forgot Password, Reset Password
│   │   ├── user/         # Portal Pasien (Dashboard Bento Grid)
│   │   ├── dokter/       # Portal Dokter
│   │   ├── apoteker/     # Portal Apoteker & Kasir
│   │   ├── resepsionis/  # Portal Resepsionis
│   │   ├── admin/        # Portal Admin Master Data
│   │   └── errors/       # Error Pages (401, 403, 404, 500)
│   └── js/               # API Client, Toast System, Auth Guard
├── flowchart.md          # Dokumentasi Diagram Flowchart Sistem
└── README.md             # Dokumen Petunjuk Utama
```

---

## Cara Instalasi & Menjalankan Aplikasi

### 1. Prasyarat Sistem
- **Java JDK 17** atau versi terbaru
- **Apache Maven 3.x**
- **PostgreSQL Database Engine**

### 2. Konfigurasi Database PostgreSQL
Buat database bernama `db_sentra` di PostgreSQL:
```sql
CREATE DATABASE db_sentra;
```

### 3. Menjalankan Microservices Backend
Buka terminal terpisah untuk setiap service dan jalankan perintah berikut secara berurutan:

1. **Auth Service (Port 8081)**:
   ```bash
   cd auth-service
   mvn spring-boot:run
   ```
2. **Clinic Service (Port 8082)**:
   ```bash
   cd clinic-service
   mvn spring-boot:run
   ```
3. **Pharmacy Service (Port 8083)**:
   ```bash
   cd pharmacy-service
   mvn spring-boot:run
   ```
4. **API Gateway (Port 8080)**:
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

*Saat microservices pertama kali dijalankan, Hibernate akan membuat skema tabel PostgreSQL secara otomatis dan Seeder data akan mengisi data awal secara otomatis.*

### 4. Menjalankan Frontend UI
Buka file `frontend/html/auth/login.html` pada browser Anda (atau gunakan HTTP Server / Live Server).

---

