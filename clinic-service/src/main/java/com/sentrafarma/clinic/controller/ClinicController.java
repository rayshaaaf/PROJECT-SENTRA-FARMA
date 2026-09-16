package com.sentrafarma.clinic.controller;

import com.sentrafarma.clinic.entity.*;
import com.sentrafarma.clinic.payload.AntrianRequest;
import com.sentrafarma.clinic.payload.RekamMedisRequest;
import com.sentrafarma.clinic.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clinic")
@Tag(name = "Clinic Service", description = "Endpoint Layanan Medis, Poliklinik, Dokter, Pasien, Antrian, & Rekam Medis")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    // --- Poliklinik ---
    @GetMapping("/poliklinik")
    @Operation(summary = "Get Daftar Semua Poliklinik")
    public ResponseEntity<List<Poliklinik>> getPoliklinik() {
        return ResponseEntity.ok(clinicService.getAllPoliklinik());
    }

    @PostMapping("/poliklinik")
    @Operation(summary = "Tambah Poliklinik Baru")
    public ResponseEntity<Poliklinik> createPoliklinik(@RequestBody Poliklinik poli) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.createPoliklinik(poli));
    }

    // --- Dokter ---
    @GetMapping("/dokter")
    @Operation(summary = "Get Daftar Dokter (Semua / Filter Poli)")
    public ResponseEntity<List<Dokter>> getDokter(@RequestParam(required = false) Long poliId) {
        if (poliId != null) {
            return ResponseEntity.ok(clinicService.getDokterByPoli(poliId));
        }
        return ResponseEntity.ok(clinicService.getAllDokter());
    }

    @GetMapping("/dokter/{id}")
    @Operation(summary = "Get Detail Dokter by ID")
    public ResponseEntity<Dokter> getDokterById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getDokterById(id));
    }

    @GetMapping("/dokter/user/{userId}")
    @Operation(summary = "Get Detail Dokter by User ID")
    public ResponseEntity<Dokter> getDokterByUserId(@PathVariable Long userId) {
        return clinicService.getDokterByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/dokter/{id}/status")
    @Operation(summary = "Update Status Duty Dokter")
    public ResponseEntity<Dokter> updateDokterStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(clinicService.updateDokterStatus(id, status));
    }

    @GetMapping("/dokter/{id}/jadwal")
    @Operation(summary = "Get Jadwal Jaga Dokter")
    public ResponseEntity<List<JadwalDokter>> getJadwalDokter(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getJadwalByDokter(id));
    }

    // --- Pasien ---
    @GetMapping("/pasien")
    @Operation(summary = "Get Daftar Pasien (Search / Pagination)")
    public ResponseEntity<?> getPasien(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(clinicService.searchPasien(search, page, limit));
        }
        return ResponseEntity.ok(clinicService.getAllPasien());
    }

    @GetMapping("/pasien/{id}")
    @Operation(summary = "Get Pasien by ID")
    public ResponseEntity<Pasien> getPasienById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getPasienById(id));
    }

    @GetMapping("/pasien/user/{userId}")
    @Operation(summary = "Get Pasien by User ID")
    public ResponseEntity<Pasien> getPasienByUserId(@PathVariable Long userId) {
        return clinicService.getPasienByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pasien")
    @Operation(summary = "Tambah/Register Profile Pasien")
    public ResponseEntity<Pasien> createPasien(@RequestBody Pasien pasien) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.savePasien(pasien));
    }

    @PutMapping("/pasien/{id}")
    @Operation(summary = "Update Profile Pasien by ID")
    public ResponseEntity<Pasien> updatePasienById(@PathVariable Long id, @RequestBody Pasien pasien) {
        pasien.setId(id);
        return ResponseEntity.ok(clinicService.savePasien(pasien));
    }

    @PutMapping("/pasien/user/{userId}")
    @Operation(summary = "Update Profile Pasien by User ID")
    public ResponseEntity<Pasien> updatePasienByUserId(@PathVariable Long userId, @RequestBody Pasien pasien) {
        pasien.setUserId(userId);
        return ResponseEntity.ok(clinicService.savePasien(pasien));
    }

    // --- Antrian ---
    @PostMapping("/antrian")
    @Operation(summary = "Ambil Tiket Antrian Digital (Online/Offline)")
    public ResponseEntity<Antrian> createAntrian(@Valid @RequestBody AntrianRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.createAntrian(request));
    }

    @GetMapping("/antrian/hari-ini")
    @Operation(summary = "Get Daftar Antrian Hari Ini (Opsional filter Dokter)")
    public ResponseEntity<List<Antrian>> getAntrianHariIni(@RequestParam(required = false) Long dokterId) {
        return ResponseEntity.ok(clinicService.getAntrianHariIni(dokterId));
    }

    @GetMapping("/antrian/pasien/{pasienId}")
    @Operation(summary = "Get Antrian Riwayat Pasien")
    public ResponseEntity<List<Antrian>> getAntrianPasien(@PathVariable Long pasienId) {
        return ResponseEntity.ok(clinicService.getAntrianByPasien(pasienId));
    }

    @PatchMapping("/antrian/{id}/status")
    @Operation(summary = "Update Status Antrian (MENUNGGU, DIPANGGIL, DIPERIKSA, SELESAI, BATAL)")
    public ResponseEntity<Antrian> updateAntrianStatus(
            @PathVariable Long id, 
            @RequestParam(required = false) String status,
            @RequestBody(required = false) Map<String, String> body) {
        String newStatus = status;
        if ((newStatus == null || newStatus.isBlank()) && body != null) {
            newStatus = body.get("status");
        }
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("Status antrian wajib diisi");
        }
        return ResponseEntity.ok(clinicService.updateAntrianStatus(id, newStatus));
    }

    @DeleteMapping("/antrian/all")
    @Operation(summary = "Hapus Semua Data Antrian & Rekam Medis Seed")
    public ResponseEntity<Void> deleteAllAntrian() {
        clinicService.deleteAllAntrian();
        return ResponseEntity.noContent().build();
    }

    // --- Rekam Medis ---
    @PostMapping("/rekam-medis")
    @Operation(summary = "Input Rekam Medis & Tanda Vital oleh Dokter")
    public ResponseEntity<RekamMedis> createRekamMedis(@Valid @RequestBody RekamMedisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.createRekamMedis(request));
    }

    @GetMapping("/rekam-medis/pasien/{pasienId}")
    @Operation(summary = "Get Timeline Rekam Medis Pasien")
    public ResponseEntity<List<RekamMedis>> getRekamMedisPasien(@PathVariable Long pasienId) {
        return ResponseEntity.ok(clinicService.getRekamMedisPasien(pasienId));
    }

    @GetMapping("/rekam-medis/{id}/tanda-vital")
    @Operation(summary = "Get Detail Tanda Vital Rekam Medis")
    public ResponseEntity<TandaVital> getTandaVital(@PathVariable Long id) {
        return clinicService.getTandaVitalByRekamMedis(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/rekam-medis/{id}")
    @Operation(summary = "Soft Delete Rekam Medis")
    public ResponseEntity<Map<String, String>> deleteRekamMedis(@PathVariable Long id) {
        clinicService.deleteRekamMedis(id);
        return ResponseEntity.ok(Map.of("message", "Rekam medis berhasil dihapus (soft delete)."));
    }

    // --- Live Chat ---
    @PostMapping("/chat/messages")
    @Operation(summary = "Kirim Pesan Live Chat Sesi Konsultasi Dokter")
    public ResponseEntity<ChatMessage> sendChatMessage(@RequestBody ChatMessage msg) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.saveChatMessage(msg));
    }

    @GetMapping("/chat/messages")
    @Operation(summary = "Get History Pesan Live Chat Sesi Konsultasi")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@RequestParam String sessionId) {
        return ResponseEntity.ok(clinicService.getChatHistory(sessionId));
    }

    @DeleteMapping("/chat/messages")
    @Operation(summary = "Bersihkan/Hapus History Pesan Live Chat Sesi Konsultasi")
    public ResponseEntity<Map<String, String>> deleteChatHistory(@RequestParam String sessionId) {
        clinicService.deleteChatHistory(sessionId);
        return ResponseEntity.ok(Map.of("message", "History pesan berhasil dibersihkan."));
    }

    @GetMapping("/chat/sessions")
    @Operation(summary = "Get Daftar Sesi Chat Aktif untuk Dokter")
    public ResponseEntity<List<String>> getDoctorChatSessions(@RequestParam Long doctorId) {
        return ResponseEntity.ok(clinicService.getDoctorChatSessions(doctorId));
    }
}
