# Walkthrough - Penjelasan Proteksi Auth Guard & Verifikasi Pembukaan Halaman

Penjelasan lengkap mengenai cara kerja proteksi **Auth Guard** ([`auth-guard.js`](file:///c:/Users/Hype/OneDrive/Dokumen/PELATIHAN/PROJECT%20JAVA%202%20BULAN/PROJECT%20SENTRA%20FARMA/frontend/js/auth-guard.js)) pada aplikasi Sentra Farma:

---

## 💡 Mengapa Dashboard Sempat Bisa Terbuka di Browser Anda?

Browser (seperti Chrome/Edge) **menyimpan token login (`jwt_token`) & data pengguna (`user_data`) di `localStorage`** setelah Anda pernah login sebelumnya.

- **Jika Anda Sudah Login Sebelumnya**: `auth-guard.js` mendeteksi bahwa token login Anda masih tersimpan aktif di browser, sehingga sistem mengizinkan Anda langsung membuka `dashboard.html` tanpa harus mengetik ulang password.
- **Jika Anda Menekan Tombol "Logout" atau Mengosongkan Sesi (`localStorage`)**: `auth-guard.js` akan **seketika memblokir dan mengarahkan browser ke Halaman Login ([`login.html`](file:///c:/Users/Hype/OneDrive/Dokumen/PELATIHAN/PROJECT%20JAVA%202%20BULAN/PROJECT%20SENTRA%20FARMA/frontend/html/auth/login.html))** sebelum ada isi halaman yang sempat tampil.

---

## 🧪 Bukti Pengujian Pengalihan (Verification)

Mencoba membuka `http://127.0.0.1:5500/frontend/html/user/dashboard.html` tanpa sesi login (atau setelah logout) -> Browser secara otomatis dan seketika dialihkan kembali ke Halaman Login (`login.html`).

![Hasil Redirect Auth Guard ke Halaman Login](file:///C:/Users/Hype/.gemini/antigravity-ide/brain/00f67975-eab8-47a2-9600-f745e0690e3a/login_redirect_result_1789357529619.png)
