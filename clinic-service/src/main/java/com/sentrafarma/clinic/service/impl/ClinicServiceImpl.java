package com.sentrafarma.clinic.service.impl;

import com.sentrafarma.clinic.entity.*;
import com.sentrafarma.clinic.payload.AntrianRequest;
import com.sentrafarma.clinic.payload.RekamMedisRequest;
import com.sentrafarma.clinic.repository.*;
import com.sentrafarma.clinic.service.ClinicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {

    private final PoliklinikRepository poliklinikRepository;
    private final DokterRepository dokterRepository;
    private final JadwalDokterRepository jadwalDokterRepository;
    private final PasienRepository pasienRepository;
    private final AntrianRepository antrianRepository;
    private final RekamMedisRepository rekamMedisRepository;
    private final TandaVitalRepository tandaVitalRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ClinicServiceImpl(PoliklinikRepository poliklinikRepository,
                             DokterRepository dokterRepository,
                             JadwalDokterRepository jadwalDokterRepository,
                             PasienRepository pasienRepository,
                             AntrianRepository antrianRepository,
                             RekamMedisRepository rekamMedisRepository,
                             TandaVitalRepository tandaVitalRepository,
                             ChatMessageRepository chatMessageRepository) {
        this.poliklinikRepository = poliklinikRepository;
        this.dokterRepository = dokterRepository;
        this.jadwalDokterRepository = jadwalDokterRepository;
        this.pasienRepository = pasienRepository;
        this.antrianRepository = antrianRepository;
        this.rekamMedisRepository = rekamMedisRepository;
        this.tandaVitalRepository = tandaVitalRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    // --- Poliklinik ---
    @Override
    public List<Poliklinik> getAllPoliklinik() {
        return poliklinikRepository.findAll();
    }

    @Override
    public Poliklinik createPoliklinik(Poliklinik poli) {
        return poliklinikRepository.save(poli);
    }

    // --- Dokter & Jadwal ---
    @Override
    public List<Dokter> getAllDokter() {
        return dokterRepository.findAll();
    }

    @Override
    public Dokter getDokterById(Long id) {
        return dokterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dokter tidak ditemukan dengan ID: " + id));
    }

    @Override
    public Optional<Dokter> getDokterByUserId(Long userId) {
        return dokterRepository.findByUserId(userId);
    }

    @Override
    public List<Dokter> getDokterByPoli(Long poliId) {
        return dokterRepository.findByPoliklinikId(poliId);
    }

    @Override
    public Dokter updateDokterStatus(Long dokterId, String status) {
        Dokter dokter = getDokterById(dokterId);
        dokter.setStatusDuty(status);
        return dokterRepository.save(dokter);
    }

    @Override
    public List<JadwalDokter> getJadwalByDokter(Long dokterId) {
        return jadwalDokterRepository.findByDokterId(dokterId);
    }

    // --- Pasien ---
    @Override
    public List<Pasien> getAllPasien() {
        return pasienRepository.findAll();
    }

    @Override
    public Pasien getPasienById(Long id) {
        return pasienRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pasien tidak ditemukan dengan ID: " + id));
    }

    @Override
    public Optional<Pasien> getPasienByUserId(Long userId) {
        return pasienRepository.findByUserId(userId);
    }

    @Override
    public Pasien savePasien(Pasien pasien) {
        if (pasien.getTanggalLahir() != null && !pasien.getTanggalLahir().trim().isEmpty()) {
            try {
                java.time.LocalDate dob = java.time.LocalDate.parse(pasien.getTanggalLahir().trim());
                if (dob.isAfter(java.time.LocalDate.now())) {
                    throw new IllegalArgumentException("Tanggal lahir pasien (" + pasien.getTanggalLahir() + ") tidak boleh di masa depan. Silakan pilih tanggal hari ini atau sebelumnya.");
                }
            } catch (java.time.format.DateTimeParseException e) {
                // Ignore parse errors if custom string
            }
        }
        return pasienRepository.save(pasien);
    }

    @Override
    public Page<Pasien> searchPasien(String keyword, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id").descending());
        return pasienRepository.findByNamaLengkapContainingIgnoreCaseOrNikContaining(keyword, keyword, pageable);
    }

    // --- Antrian ---
    @Override
    @Transactional
    public Antrian createAntrian(AntrianRequest request) {
        Pasien pasien = getPasienById(request.getPasienId());
        Dokter dokter = getDokterById(request.getDokterId());
        Poliklinik poli = poliklinikRepository.findById(request.getPoliklinikId())
                .orElseThrow(() -> new IllegalArgumentException("Poliklinik tidak ditemukan"));

        String tgl = (request.getTanggalBerobat() != null && !request.getTanggalBerobat().isBlank())
                ? request.getTanggalBerobat()
                : ((request.getTanggalAntrian() != null && !request.getTanggalAntrian().isBlank()) ? request.getTanggalAntrian() : LocalDate.now().toString());

        try {
            LocalDate bookingDate = LocalDate.parse(tgl);
            if (bookingDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Tanggal berobat (" + tgl + ") tidak boleh di masa lalu. Silakan pilih tanggal hari ini atau mendatang.");
            }
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Format tanggal berobat tidak valid (Gunakan YYYY-MM-DD)");
        }

        long count = antrianRepository.countByTanggalBerobatAndPoliklinikId(tgl, poli.getId());
        String codePrefix = "A";
        if (poli.getNamaPoli() != null && !poli.getNamaPoli().isBlank()) {
            String name = poli.getNamaPoli().trim();
            if (name.toLowerCase().startsWith("poli ") && name.length() > 5) {
                codePrefix = name.substring(5, 6).toUpperCase();
            } else {
                codePrefix = name.substring(0, 1).toUpperCase();
            }
        }
        String queueNum = String.format("%s-%02d", codePrefix, count + 1);

        Antrian antrian = Antrian.builder()
                .nomorAntrian(queueNum)
                .pasien(pasien)
                .dokter(dokter)
                .poliklinik(poli)
                .tanggalBerobat(tgl)
                .status("MENUNGGU")
                .tipe(request.getTipe() != null ? request.getTipe() : "ONLINE")
                .build();

        return antrianRepository.save(antrian);
    }

    @Override
    public List<Antrian> getAntrianHariIni(Long dokterId) {
        String today = LocalDate.now().toString();
        if (dokterId != null) {
            return antrianRepository.findByTanggalBerobatAndDokterId(today, dokterId);
        }
        return antrianRepository.findByTanggalBerobat(today);
    }

    @Override
    public List<Antrian> getAntrianByPasien(Long pasienId) {
        return antrianRepository.findByPasienIdOrderByCreatedAtDesc(pasienId);
    }

    @Override
    @Transactional
    public Antrian updateAntrianStatus(Long antrianId, String status) {
        Antrian antrian = antrianRepository.findById(antrianId)
                .orElseThrow(() -> new IllegalArgumentException("Antrian tidak ditemukan"));
        antrian.setStatus(status);
        return antrianRepository.save(antrian);
    }

    @Override
    @Transactional
    public void deleteAllAntrian() {
        tandaVitalRepository.deleteAll();
        rekamMedisRepository.deleteAll();
        antrianRepository.deleteAll();
    }

    // --- Rekam Medis & Tanda Vital ---
    @Override
    @Transactional
    public RekamMedis createRekamMedis(RekamMedisRequest req) {
        Pasien pasien = getPasienById(req.getPasienId());
        Dokter dokter = getDokterById(req.getDokterId());

        Antrian antrian = null;
        if (req.getAntrianId() != null) {
            antrian = antrianRepository.findById(req.getAntrianId()).orElse(null);
            if (antrian != null) {
                antrian.setStatus("SELESAI");
                antrianRepository.save(antrian);
            }
        }

        RekamMedis rm = RekamMedis.builder()
                .antrian(antrian)
                .pasien(pasien)
                .dokter(dokter)
                .keluhanUtama(req.getKeluhanUtama())
                .diagnosaIcd10(req.getDiagnosaIcd10())
                .catatanDokter(req.getCatatanDokter())
                .isDeleted(false)
                .build();

        rm = rekamMedisRepository.save(rm);

        if (req.getTekananDarahSistol() != null || req.getSuhuTubuh() != null || req.getBeratBadan() != null) {
            TandaVital tv = TandaVital.builder()
                    .rekamMedis(rm)
                    .tekananDarahSistol(req.getTekananDarahSistol())
                    .tekananDarahDiastol(req.getTekananDarahDiastol())
                    .detakJantung(req.getDetakJantung())
                    .suhuTubuh(req.getSuhuTubuh())
                    .beratBadan(req.getBeratBadan())
                    .tinggiTubuh(req.getTinggiTubuh())
                    .build();
            tandaVitalRepository.save(tv);
        }

        return rm;
    }

    @Override
    public List<RekamMedis> getRekamMedisPasien(Long pasienId) {
        return rekamMedisRepository.findByPasienIdAndIsDeletedFalseOrderByCreatedAtDesc(pasienId);
    }

    @Override
    public Optional<TandaVital> getTandaVitalByRekamMedis(Long rekamMedisId) {
        return tandaVitalRepository.findByRekamMedisId(rekamMedisId);
    }

    @Override
    @Transactional
    public void deleteRekamMedis(Long id) {
        RekamMedis rm = rekamMedisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rekam medis tidak ditemukan"));
        rm.setIsDeleted(true);
        rekamMedisRepository.save(rm);
    }

    // --- Live Chat ---
    @Override
    public ChatMessage saveChatMessage(ChatMessage msg) {
        return chatMessageRepository.save(msg);
    }

    @Override
    public List<ChatMessage> getChatHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Override
    public List<String> getDoctorChatSessions(Long doctorId) {
        String pattern = "doc_" + doctorId;
        return chatMessageRepository.findDistinctSessionIdsByDoctor(doctorId, pattern);
    }
}
