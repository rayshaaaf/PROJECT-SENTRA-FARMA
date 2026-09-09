# Sentra Farma — Sistem Manajemen Klinik & Apotek Terintegrasi

Platform Sistem Manajemen Klinik dan Apotek Terintegrasi Berbasis Web dengan Arsitektur **Microservices Monorepo** (Java 17 Spring Boot, PostgreSQL, Tailwind CSS, Vanilla JS).

---

## 📋 Fitur Utama System

1. **Multi-Role Based Access Control (RBAC 5 Role)**:
   - **Pasien**: Portal Bento Grid (Tanda Vital, Live Queue Tracker, Timeline Medis, Katalog Apotek, Live Chat Dokter, Profil QR Code).
   - **Dokter**: Queue Antrian Poli, Input Rekam Medis & Tanda Vital (ICD-10), Penerbitan E-Resep Digital, Consultation Chat.
   - **Apoteker**: Penebusan E-Resep & Potong Stok Otomatis, Transaksi Penjualan Obat Bebas Kasir (POS), Inventory Management (Soft Delete, Low Stock Alert).
   - **Resepsionis**: Pendaftaran Pasien Walk-in (Offline), Pemanggilan & Status Antrian, Kelola Jadwal Dokter Jaga.
   - **Admin**: Master Data CRUD Users & RBAC, Reports Analytics & Export Laporan (PDF/Excel).

2. **Compliance Ketentuan S1**:
   - Arsitektur Monorepo Microservices (`api-gateway`, `auth-service`, `clinic-service`, `pharmacy-service`, `frontend`).
   - Struktur Package Standar: `entity` (Persistence Domain Entities) & `payload` (Request/Response Transfer Objects).
   - PostgreSQL Database (`db_sentra`) dengan 6+ Tabel Utama, 5 Jenis Relasi (1:1, 1:N, N:1, N:N), Timestamps `created_at` & `updated_at`, dan Soft Delete pada 3 tabel (`users`, `rekam_medis`, `obat`).
   - Seeding Data Awal otomatis (20+ data per tabel utama).
   - Real-time Search, Filter, Sort, & Pagination bekerja simultan.
   - Global Exception Handling (JSON standar 400, 401, 403, 404, 422, 500) + Halaman Error Fallback.
   - Toast Notification & Validasi Form Real-time.

---

## 🔑 Kredensial Akun Demo (All Password: `password123`)

| Role | Email Demo | Password |
|---|---|---|
| **Admin** | `admin@sentrafarma.com` | `password123` |
| **Dokter** | `dr.hendra@sentrafarma.com` | `password123` |
| **Apoteker** | `apoteker@sentrafarma.com` | `password123` |
| **Resepsionis** | `resepsionis@sentrafarma.com` | `password123` |
| **Pasien** | `pasien1@gmail.com` | `password123` |

---

## 📂 Struktur Folder Projek Monorepo

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
│   │   └── errors/       # 401, 403, 404, 500 Error Pages
│   └── js/               # API Client, Toast System, Auth Guard
├── flowchart.md          # Dokumentasi Diagram Flowchart Sistem
└── README.md             # Dokumen Petunjuk Utama
```

---

## 🛠️ Cara Instalasi & Menjalankan Aplikasi

### 1. Prasyarat System:
- Java JDK 17 atau lebih baru
- Apache Maven 3.x
- PostgreSQL Database Engine (Port default: `5433` atau `5432`)

### 2. Konfigurasi Database PostgreSQL:
Buat database bernama `db_sentra` di PostgreSQL:
```sql
CREATE DATABASE db_sentra;
```

### 3. Menjalankan Microservices Backend:
Buka terminal terpisah untuk setiap service dan jalankan perintah:

- **Auth Service (Port 8081)**:
  ```bash
  cd auth-service
  mvn spring-boot:run
  ```
- **Clinic Service (Port 8082)**:
  ```bash
  cd clinic-service
  mvn spring-boot:run
  ```
- **Pharmacy Service (Port 8083)**:
  ```bash
  cd pharmacy-service
  mvn spring-boot:run
  ```
- **API Gateway (Port 8080)**:
  ```bash
  cd api-gateway
  mvn spring-boot:run
  ```

*Saat microservices pertama kali menyala, Hibernate akan otomatis membuat skema tabel PostgreSQL dan `DataSeeder` akan mengisi 20+ data awal per tabel utama!*

### 4. Menjalankan Frontend UI:
Buka file `frontend/html/auth/login.html` di browser Anda (atau gunakan live server / web server pada port `8084`).

---

## 📖 Swagger / OpenAPI Documentation

Setiap service dilengkapi dengan Swagger UI yang dapat diakses di:
- Auth Service: `http://localhost:8081/swagger-ui.html`
- Clinic Service: `http://localhost:8082/swagger-ui.html`
- Pharmacy Service: `http://localhost:8083/swagger-ui.html`
