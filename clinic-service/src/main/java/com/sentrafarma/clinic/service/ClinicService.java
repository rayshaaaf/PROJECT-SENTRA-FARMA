package com.sentrafarma.clinic.service;

import com.sentrafarma.clinic.entity.*;
import com.sentrafarma.clinic.payload.AntrianRequest;
import com.sentrafarma.clinic.payload.RekamMedisRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ClinicService {

    // Poliklinik
    List<Poliklinik> getAllPoliklinik();
    Poliklinik createPoliklinik(Poliklinik poli);

    // Dokter & Jadwal
    List<Dokter> getAllDokter();
    Dokter getDokterById(Long id);
    Optional<Dokter> getDokterByUserId(Long userId);
    List<Dokter> getDokterByPoli(Long poliId);
    Dokter updateDokterStatus(Long dokterId, String status);
    List<JadwalDokter> getJadwalByDokter(Long dokterId);

    // Pasien
    List<Pasien> getAllPasien();
    Pasien getPasienById(Long id);
    Optional<Pasien> getPasienByUserId(Long userId);
    Pasien savePasien(Pasien pasien);
    Page<Pasien> searchPasien(String keyword, int page, int limit);

    // Antrian
    Antrian createAntrian(AntrianRequest request);
    List<Antrian> getAntrianHariIni(Long dokterId);
    List<Antrian> getAntrianByPasien(Long pasienId);
    Antrian updateAntrianStatus(Long antrianId, String status);
    void deleteAllAntrian();

    // Rekam Medis & Tanda Vital
    RekamMedis createRekamMedis(RekamMedisRequest req);
    List<RekamMedis> getRekamMedisPasien(Long pasienId);
    Optional<TandaVital> getTandaVitalByRekamMedis(Long rekamMedisId);
    void deleteRekamMedis(Long id);

    // Live Chat
    ChatMessage saveChatMessage(ChatMessage msg);
    List<ChatMessage> getChatHistory(String sessionId);
    void deleteChatHistory(String sessionId);
    List<String> getDoctorChatSessions(Long doctorId);
}
