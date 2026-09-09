package com.sentrafarma.clinic.config;

import com.sentrafarma.clinic.entity.*;
import com.sentrafarma.clinic.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final PoliklinikRepository poliklinikRepository;
    private final DokterRepository dokterRepository;
    private final JadwalDokterRepository jadwalDokterRepository;
    private final PasienRepository pasienRepository;
    private final AntrianRepository antrianRepository;
    private final RekamMedisRepository rekamMedisRepository;
    private final TandaVitalRepository tandaVitalRepository;

    public DataSeeder(PoliklinikRepository poliklinikRepository,
                      DokterRepository dokterRepository,
                      JadwalDokterRepository jadwalDokterRepository,
                      PasienRepository pasienRepository,
                      AntrianRepository antrianRepository,
                      RekamMedisRepository rekamMedisRepository,
                      TandaVitalRepository tandaVitalRepository) {
        this.poliklinikRepository = poliklinikRepository;
        this.dokterRepository = dokterRepository;
        this.jadwalDokterRepository = jadwalDokterRepository;
        this.pasienRepository = pasienRepository;
        this.antrianRepository = antrianRepository;
        this.rekamMedisRepository = rekamMedisRepository;
        this.tandaVitalRepository = tandaVitalRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (poliklinikRepository.count() > 0) {
            log.info("Clinic Service: Database sudah terisi. Seeding dilewati.");
            return;
        }

        log.info("Clinic Service: Mengisi Data Awal (Seeding Poliklinik, Dokter, Pasien, Antrian, Rekam Medis)...");

        // 1. Poliklinik
        Poliklinik poliUmum = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Umum").deskripsi("Layanan kesehatan dasar dan konsultasi dokter umum").ruangan("Lantai 1 - Ruang 101").build());
        Poliklinik poliAnak = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Anak").deskripsi("Spesialis tumbuh kembang dan penyakit anak").ruangan("Lantai 1 - Ruang 102").build());
        Poliklinik poliDalam = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Penyakit Dalam").deskripsi("Spesialis organ dalam, diabetes, hipertensi").ruangan("Lantai 2 - Ruang 201").build());
        Poliklinik poliTht = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli THT").deskripsi("Spesialis telinga, hidung, dan tenggorokan").ruangan("Lantai 2 - Ruang 202").build());
        Poliklinik poliKulit = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Kulit & Kelamin").deskripsi("Perawatan kulit, alergi, dan dermatologi").ruangan("Lantai 2 - Ruang 203").build());

        // 2. Dokter
        Dokter drHendra = dokterRepository.save(Dokter.builder().userId(6L).namaDokter("dr. Hendra Wijaya, Sp.PD").sip("SIP-2026/001/PD").spesialisasi("Spesialis Penyakit Dalam").poliklinik(poliDalam).statusDuty("ON_DUTY").build());
        Dokter drAnisa = dokterRepository.save(Dokter.builder().userId(7L).namaDokter("dr. Anisa Rahmawati, Sp.A").sip("SIP-2026/002/SPA").spesialisasi("Spesialis Anak").poliklinik(poliAnak).statusDuty("ON_DUTY").build());
        Dokter drBudi = dokterRepository.save(Dokter.builder().userId(8L).namaDokter("dr. Budi Santoso, Sp.THT").sip("SIP-2026/003/THT").spesialisasi("Spesialis THT").poliklinik(poliTht).statusDuty("ON_DUTY").build());
        Dokter drCitra = dokterRepository.save(Dokter.builder().userId(9L).namaDokter("dr. Citra Kirana, Sp.KK").sip("SIP-2026/004/KK").spesialisasi("Spesialis Kulit").poliklinik(poliKulit).statusDuty("ON_DUTY").build());
        Dokter drDian = dokterRepository.save(Dokter.builder().userId(10L).namaDokter("dr. Dian Sastro, Sp.OG").sip("SIP-2026/005/OG").spesialisasi("Spesialis umum").poliklinik(poliUmum).statusDuty("ON_DUTY").build());

        // 3. Jadwal Dokter
        jadwalDokterRepository.saveAll(List.of(
                JadwalDokter.builder().dokter(drHendra).hari("Senin - Jumat").jamMulai("08:00").jamSelesai("12:00").build(),
                JadwalDokter.builder().dokter(drAnisa).hari("Senin - Sabtu").jamMulai("09:00").jamSelesai("14:00").build(),
                JadwalDokter.builder().dokter(drBudi).hari("Selasa & Kamis").jamMulai("13:00").jamSelesai("17:00").build(),
                JadwalDokter.builder().dokter(drCitra).hari("Rabu & Jumat").jamMulai("10:00").jamSelesai("15:00").build(),
                JadwalDokter.builder().dokter(drDian).hari("Senin - Minggu").jamMulai("08:00").jamSelesai("16:00").build()
        ));

        // 4. Pasien (20 Pasien)
        List<Pasien> pasiens = List.of(
                Pasien.builder().userId(11L).nik("3171010101900001").namaLengkap("Ahmad Dahlan").email("pasien1@gmail.com").noTelepon("082100010001").tanggalLahir("1990-01-15").alamat("Jl. Sudirman No. 12, Jakarta").jenisKelamin("Laki-laki").golonganDarah("O").build(),
                Pasien.builder().userId(12L).nik("3171010202920002").namaLengkap("Budi Gunawan").email("pasien2@gmail.com").noTelepon("082100010002").tanggalLahir("1992-02-20").alamat("Jl. Gatot Subroto No. 45, Jakarta").jenisKelamin("Laki-laki").golonganDarah("A").build(),
                Pasien.builder().userId(13L).nik("3171010303950003").namaLengkap("Cici Paramida").email("pasien3@gmail.com").noTelepon("082100010003").tanggalLahir("1995-03-25").alamat("Jl. Rasuna Said No. 8, Jakarta").jenisKelamin("Perempuan").golonganDarah("B").build(),
                Pasien.builder().userId(14L).nik("3171010404880004").namaLengkap("Doni Monardo").email("pasien4@gmail.com").noTelepon("082100010004").tanggalLahir("1988-04-10").alamat("Jl. MH Thamrin No. 10, Jakarta").jenisKelamin("Laki-laki").golonganDarah("AB").build(),
                Pasien.builder().userId(15L).nik("3171010505970005").namaLengkap("Eka Kurniawan").email("pasien5@gmail.com").noTelepon("082100010005").tanggalLahir("1997-05-05").alamat("Jl. Kebon Jeruk No. 3, Jakarta").jenisKelamin("Laki-laki").golonganDarah("O").build(),
                Pasien.builder().userId(16L).nik("3171010606990006").namaLengkap("Fani Rahmawati").email("pasien6@gmail.com").noTelepon("082100010006").tanggalLahir("1999-06-18").alamat("Jl. Tebet Raya No. 22, Jakarta").jenisKelamin("Perempuan").golonganDarah("A").build(),
                Pasien.builder().userId(17L).nik("3171010707910007").namaLengkap("Gilang Dirga").email("pasien7@gmail.com").noTelepon("082100010007").tanggalLahir("1991-07-07").alamat("Jl. Pancoran No. 5, Jakarta").jenisKelamin("Laki-laki").golonganDarah("B").build(),
                Pasien.builder().userId(18L).nik("3171010808940008").namaLengkap("Hana Pertiwi").email("pasien8@gmail.com").noTelepon("082100010008").tanggalLahir("1994-08-12").alamat("Jl. Kemang Raya No. 19, Jakarta").jenisKelamin("Perempuan").golonganDarah("O").build(),
                Pasien.builder().userId(19L).nik("3171010909930009").namaLengkap("Indra Bekti").email("pasien9@gmail.com").noTelepon("082100010009").tanggalLahir("1993-09-30").alamat("Jl. Blok M No. 88, Jakarta").jenisKelamin("Laki-laki").golonganDarah("AB").build(),
                Pasien.builder().userId(20L).nik("3171011010850010").namaLengkap("Joko Widodo").email("pasien10@gmail.com").noTelepon("082100010010").tanggalLahir("1985-10-10").alamat("Jl. Solo No. 1, Surakarta").jenisKelamin("Laki-laki").golonganDarah("O").build()
        );
        List<Pasien> savedPasiens = pasienRepository.saveAll(pasiens);

        // 5. Antrian & Rekam Medis (Clean environment - no initial dummy queues)
        /*
        String today = LocalDate.now().toString();
        for (int i = 0; i < savedPasiens.size(); i++) {
            Pasien p = savedPasiens.get(i);
            Dokter d = (i % 2 == 0) ? drHendra : drDian;
            Poliklinik poli = d.getPoliklinik();

            Antrian antrian = antrianRepository.save(Antrian.builder()
                    .nomorAntrian(String.format("%s-%02d", poli.getNamaPoli().substring(0, 1), i + 1))
                    .pasien(p)
                    .dokter(d)
                    .poliklinik(poli)
                    .tanggalBerobat(today)
                    .status(i < 3 ? "MENUNGGU" : "SELESAI")
                    .tipe(i % 3 == 0 ? "OFFLINE" : "ONLINE")
                    .build());
        }
        */

        log.info("Clinic Service: Berhasil menyimpan data awal Poliklinik, Dokter, Pasien, Antrian, Rekam Medis, & Tanda Vital!");
    }
}
