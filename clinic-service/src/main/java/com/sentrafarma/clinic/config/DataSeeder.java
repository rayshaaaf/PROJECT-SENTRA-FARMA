package com.sentrafarma.clinic.config;

import com.sentrafarma.clinic.entity.*;
import com.sentrafarma.clinic.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("unused")
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
            log.info("Clinic Service: Database sudah terisi. Memeriksa data Dokter...");
            List<Dokter> doctors = dokterRepository.findAll();
            for (Dokter d : doctors) {
                if (d.getNamaDokter() != null && d.getNamaDokter().contains("Dian Sastro") && d.getPoliklinik() != null && d.getPoliklinik().getId() == 1L) {
                    d.setNamaDokter("dr. Arisandy Pratama, M.Ked");
                    d.setSpesialisasi("Dokter Umum");
                    dokterRepository.save(d);
                    log.info("Berhasil menyelaraskan data Dokter Poli Umum: dr. Arisandy Pratama, M.Ked (Dokter Umum)");
                }
            }
            return;
        }

        log.info("Clinic Service: Mengisi Data Awal Klinik 24 Jam (Seeding Poliklinik, Dokter, Jadwal, Pasien, Antrian)...");

        // 1. Poliklinik Realistis Klinik 24 Jam
        Poliklinik poliUmum = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Umum").deskripsi("Layanan kesehatan dasar, berobat jalan, dan konsultasi kesehatan umum").ruangan("Ruang 01 (Lantai 1)").build());
        Poliklinik poliGigi = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Gigi & Mulut").deskripsi("Perawatan gigi, scaling karang gigi, penambalan, dan pencabutan gigi").ruangan("Ruang 02 (Lantai 1)").build());
        Poliklinik poliKia = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli KIA & KB").deskripsi("Pemeriksaan kehamilan (ANC), layanan KB, tumbuh kembang anak, dan imunisasi").ruangan("Ruang 03 (Lantai 1)").build());
        Poliklinik poliAnak = poliklinikRepository.save(Poliklinik.builder().namaPoli("Poli Anak").deskripsi("Konsultasi tumbuh kembang, penyakit anak, imunisasi, dan nutrisi anak").ruangan("Ruang 04 (Lantai 1)").build());
        Poliklinik poliUgd = poliklinikRepository.save(Poliklinik.builder().namaPoli("Unit Tindakan & Emergency").deskripsi("Pertolongan pertama gawat darurat ringan, jahit luka, dan tindakan darurat 24 jam").ruangan("Ruang Tindakan (Lantai 1)").build());

        // 2. Dokter (12 Dokter terbagi 3 Shift 24 Jam)
        Dokter drArisandy = dokterRepository.save(Dokter.builder().userId(6L).namaDokter("dr. Arisandy Pratama, M.Ked").sip("SIP-2026/001/UMUM").spesialisasi("Dokter Umum").poliklinik(poliUmum).statusDuty("ON_DUTY").build());
        Dokter drSabrina = dokterRepository.save(Dokter.builder().userId(7L).namaDokter("dr. Sabrina Prameshwari").sip("SIP-2026/002/UMUM").spesialisasi("Dokter Umum").poliklinik(poliUmum).statusDuty("ON_DUTY").build());
        Dokter drGhazi = dokterRepository.save(Dokter.builder().userId(8L).namaDokter("dr. Ghazi Al-Fatih").sip("SIP-2026/003/UMUM").spesialisasi("Dokter Umum").poliklinik(poliUmum).statusDuty("ON_DUTY").build());

        Dokter drgMahendra = dokterRepository.save(Dokter.builder().userId(9L).namaDokter("drg. Mahendra Varian, Sp.KG").sip("SIP-2026/004/GIGI").spesialisasi("Dokter Gigi Konservasi").poliklinik(poliGigi).statusDuty("ON_DUTY").build());
        Dokter drgZhafira = dokterRepository.save(Dokter.builder().userId(10L).namaDokter("drg. Zhafira Amalia").sip("SIP-2026/005/GIGI").spesialisasi("Dokter Gigi Umum").poliklinik(poliGigi).statusDuty("ON_DUTY").build());

        Dokter drKalila = dokterRepository.save(Dokter.builder().userId(11L).namaDokter("dr. Kalila Kirana, Dip.Derm").sip("SIP-2026/006/KIA").spesialisasi("Dokter KIA & Estetika").poliklinik(poliKia).statusDuty("ON_DUTY").build());
        Dokter drNaura = dokterRepository.save(Dokter.builder().userId(12L).namaDokter("dr. Naura Danendra").sip("SIP-2026/007/KIA").spesialisasi("Dokter KIA & Kebidanan").poliklinik(poliKia).statusDuty("ON_DUTY").build());

        Dokter drAurelia = dokterRepository.save(Dokter.builder().userId(13L).namaDokter("dr. Aurelia Savitri, Sp.A").sip("SIP-2026/008/ANAK").spesialisasi("Dokter Spesialis Anak").poliklinik(poliAnak).statusDuty("ON_DUTY").build());
        Dokter drTristan = dokterRepository.save(Dokter.builder().userId(14L).namaDokter("dr. Tristan Alamsyah, Sp.A").sip("SIP-2026/009/ANAK").spesialisasi("Dokter Spesialis Anak").poliklinik(poliAnak).statusDuty("ON_DUTY").build());

        Dokter drRaditya = dokterRepository.save(Dokter.builder().userId(15L).namaDokter("dr. Raditya Mahesa").sip("SIP-2026/010/UGD").spesialisasi("Dokter Emergency").poliklinik(poliUgd).statusDuty("ON_DUTY").build());
        Dokter drDarren = dokterRepository.save(Dokter.builder().userId(16L).namaDokter("dr. Darren Valerius").sip("SIP-2026/011/UGD").spesialisasi("Dokter Emergency").poliklinik(poliUgd).statusDuty("ON_DUTY").build());
        Dokter drClarissa = dokterRepository.save(Dokter.builder().userId(17L).namaDokter("dr. Clarissa Elvina").sip("SIP-2026/012/UGD").spesialisasi("Dokter Emergency").poliklinik(poliUgd).statusDuty("ON_DUTY").build());

        // 3. Jadwal Dokter (3 Shift 24 Jam: Pagi 07-15, Sore 15-23, Malam 23-07)
        jadwalDokterRepository.saveAll(List.of(
                // Poli Umum (24 Jam)
                JadwalDokter.builder().dokter(drArisandy).hari("Senin - Minggu").jamMulai("07:00").jamSelesai("15:00").build(),
                JadwalDokter.builder().dokter(drSabrina).hari("Senin - Minggu").jamMulai("15:00").jamSelesai("23:00").build(),
                JadwalDokter.builder().dokter(drGhazi).hari("Senin - Minggu").jamMulai("23:00").jamSelesai("07:00").build(),

                // Poli Gigi & Mulut
                JadwalDokter.builder().dokter(drgMahendra).hari("Senin - Sabtu").jamMulai("08:00").jamSelesai("15:00").build(),
                JadwalDokter.builder().dokter(drgZhafira).hari("Senin - Sabtu").jamMulai("15:00").jamSelesai("21:00").build(),

                // Poli KIA & KB
                JadwalDokter.builder().dokter(drKalila).hari("Senin - Jumat").jamMulai("08:00").jamSelesai("14:00").build(),
                JadwalDokter.builder().dokter(drNaura).hari("Senin - Sabtu").jamMulai("14:00").jamSelesai("20:00").build(),

                // Poli Anak
                JadwalDokter.builder().dokter(drAurelia).hari("Senin - Sabtu").jamMulai("08:00").jamSelesai("14:00").build(),
                JadwalDokter.builder().dokter(drTristan).hari("Senin - Sabtu").jamMulai("14:00").jamSelesai("20:00").build(),

                // Unit Tindakan & Emergency (24 Jam)
                JadwalDokter.builder().dokter(drRaditya).hari("Senin - Minggu").jamMulai("07:00").jamSelesai("15:00").build(),
                JadwalDokter.builder().dokter(drDarren).hari("Senin - Minggu").jamMulai("15:00").jamSelesai("23:00").build(),
                JadwalDokter.builder().dokter(drClarissa).hari("Senin - Minggu").jamMulai("23:00").jamSelesai("07:00").build()
        ));

        // 4. Pasien (20 Pasien Lengkap)
        List<Pasien> pasiens = List.of(
                Pasien.builder().userId(18L).nik("3171010101900001").namaLengkap("Ahmad Dahlan").email("pasien1@gmail.com").noTelepon("082100010001").tanggalLahir("1990-01-15").alamat("Jl. Sudirman No. 12, Jakarta").jenisKelamin("Laki-laki").golonganDarah("O").build(),
                Pasien.builder().userId(19L).nik("3171010202920002").namaLengkap("Budi Gunawan").email("pasien2@gmail.com").noTelepon("082100010002").tanggalLahir("1992-02-20").alamat("Jl. Gatot Subroto No. 45, Jakarta").jenisKelamin("Laki-laki").golonganDarah("A").build(),
                Pasien.builder().userId(20L).nik("3171010303950003").namaLengkap("Cici Paramida").email("pasien3@gmail.com").noTelepon("082100010003").tanggalLahir("1995-03-25").alamat("Jl. Rasuna Said No. 8, Jakarta").jenisKelamin("Perempuan").golonganDarah("B").build(),
                Pasien.builder().userId(21L).nik("3171010404880004").namaLengkap("Doni Monardo").email("pasien4@gmail.com").noTelepon("082100010004").tanggalLahir("1988-04-10").alamat("Jl. MH Thamrin No. 10, Jakarta").jenisKelamin("Laki-laki").golonganDarah("AB").build(),
                Pasien.builder().userId(22L).nik("3171010505970005").namaLengkap("Eka Kurniawan").email("pasien5@gmail.com").noTelepon("082100010005").tanggalLahir("1997-05-05").alamat("Jl. Kebon Jeruk No. 3, Jakarta").jenisKelamin("Laki-laki").golonganDarah("O").build(),
                Pasien.builder().userId(23L).nik("3171010606990006").namaLengkap("Fani Rahmawati").email("pasien6@gmail.com").noTelepon("082100010006").tanggalLahir("1999-06-18").alamat("Jl. Tebet Raya No. 22, Jakarta").jenisKelamin("Perempuan").golonganDarah("A").build(),
                Pasien.builder().userId(24L).nik("3171010707910007").namaLengkap("Gilang Dirga").email("pasien7@gmail.com").noTelepon("082100010007").tanggalLahir("1991-07-07").alamat("Jl. Pancoran No. 5, Jakarta").jenisKelamin("Laki-laki").golonganDarah("B").build(),
                Pasien.builder().userId(25L).nik("3171010808940008").namaLengkap("Hana Pertiwi").email("pasien8@gmail.com").noTelepon("082100010008").tanggalLahir("1994-08-12").alamat("Jl. Kemang Raya No. 19, Jakarta").jenisKelamin("Perempuan").golonganDarah("O").build(),
                Pasien.builder().userId(26L).nik("3171010909930009").namaLengkap("Indra Bekti").email("pasien9@gmail.com").noTelepon("082100010009").tanggalLahir("1993-09-30").alamat("Jl. Blok M No. 88, Jakarta").jenisKelamin("Laki-laki").golonganDarah("AB").build(),
                Pasien.builder().userId(27L).nik("3171011010850010").namaLengkap("Joko Widodo").email("pasien10@gmail.com").noTelepon("082100010010").tanggalLahir("1985-10-10").alamat("Jl. Solo No. 1, Surakarta").jenisKelamin("Laki-laki").golonganDarah("O").build(),
                Pasien.builder().userId(28L).nik("3171011111960011").namaLengkap("Kania Dewanti").email("pasien11@gmail.com").noTelepon("082100010011").tanggalLahir("1996-11-11").alamat("Jl. Juanda No. 15, Bandung").jenisKelamin("Perempuan").golonganDarah("A").build(),
                Pasien.builder().userId(29L).nik("3171011212890012").namaLengkap("Lukman Sardi").email("pasien12@gmail.com").noTelepon("082100010012").tanggalLahir("1989-12-12").alamat("Jl. Asia Afrika No. 7, Bandung").jenisKelamin("Laki-laki").golonganDarah("B").build(),
                Pasien.builder().userId(30L).nik("3171010101940013").namaLengkap("Maya Ahmad").email("pasien13@gmail.com").noTelepon("082100010013").tanggalLahir("1994-01-01").alamat("Jl. Dago No. 101, Bandung").jenisKelamin("Perempuan").golonganDarah("O").build(),
                Pasien.builder().userId(31L).nik("3171010202980014").namaLengkap("Naufal Rizky").email("pasien14@gmail.com").noTelepon("082100010014").tanggalLahir("1998-02-02").alamat("Jl. Riau No. 44, Bandung").jenisKelamin("Laki-laki").golonganDarah("AB").build(),
                Pasien.builder().userId(32L).nik("3171010303960015").namaLengkap("Olivia Jensen").email("pasien15@gmail.com").noTelepon("082100010015").tanggalLahir("1996-03-03").alamat("Jl. Merdeka No. 88, Surabaya").jenisKelamin("Perempuan").golonganDarah("A").build(),
                Pasien.builder().userId(33L).nik("3171010404930016").namaLengkap("Pradipta Arya").email("pasien16@gmail.com").noTelepon("082100010016").tanggalLahir("1993-04-04").alamat("Jl. Pemuda No. 12, Semarang").jenisKelamin("Laki-laki").golonganDarah("B").build(),
                Pasien.builder().userId(34L).nik("3171010505990017").namaLengkap("Qiana Naura").email("pasien17@gmail.com").noTelepon("082100010017").tanggalLahir("1999-05-05").alamat("Jl. Malioboro No. 50, Yogyakarta").jenisKelamin("Perempuan").golonganDarah("O").build(),
                Pasien.builder().userId(35L).nik("3171010606870018").namaLengkap("Reza Rahadian").email("pasien18@gmail.com").noTelepon("082100010018").tanggalLahir("1987-06-06").alamat("Jl. Solo No. 99, Yogyakarta").jenisKelamin("Laki-laki").golonganDarah("AB").build(),
                Pasien.builder().userId(36L).nik("3171010707950019").namaLengkap("Siti Badriah").email("pasien19@gmail.com").noTelepon("082100010019").tanggalLahir("1995-07-07").alamat("Jl. Diponegoro No. 3, Bali").jenisKelamin("Perempuan").golonganDarah("A").build(),
                Pasien.builder().userId(37L).nik("3171010808920020").namaLengkap("Taufik Hidayat").email("pasien20@gmail.com").noTelepon("082100010020").tanggalLahir("1992-08-08").alamat("Jl. Gajah Mada No. 1, Medan").jenisKelamin("Laki-laki").golonganDarah("O").build()
        );
        List<Pasien> savedPasiens = pasienRepository.saveAll(pasiens);

        log.info("Clinic Service: Berhasil menyimpan data 5 Poliklinik, 12 Dokter (3-Shift 24Jam), & 20 Pasien!");
    }
}
