package com.sentrafarma.auth.config;

import com.sentrafarma.auth.entity.Role;
import com.sentrafarma.auth.entity.User;
import com.sentrafarma.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Auth Service: Database sudah berisi data. Seeding dilewati.");
            return;
        }

        log.info("Auth Service: Mengisi 20+ data awal (Seeding Users)...");
        String defaultPassword = passwordEncoder.encode("password123");

        List<User> initialUsers = List.of(
                User.builder().email("admin@sentrafarma.com").password(defaultPassword).namaLengkap("Administrator System").role(Role.ADMIN).noTelepon("081234567890").isDeleted(false).build(),
                User.builder().email("admin.klinik@sentrafarma.com").password(defaultPassword).namaLengkap("Admin Operasional Klinik").role(Role.ADMIN).noTelepon("081234567891").isDeleted(false).build(),
                User.builder().email("admin.farmasi@sentrafarma.com").password(defaultPassword).namaLengkap("Admin Logistik Farmasi").role(Role.ADMIN).noTelepon("081234567892").isDeleted(false).build(),
                User.builder().email("apoteker@sentrafarma.com").password(defaultPassword).namaLengkap("apt. Bambang Sugianto, S.Farm").role(Role.APOTEKER).noTelepon("081298765432").isDeleted(false).build(),
                User.builder().email("apoteker2@sentrafarma.com").password(defaultPassword).namaLengkap("apt. Siti Nurhaliza, S.Farm").role(Role.APOTEKER).noTelepon("081311223344").isDeleted(false).build(),
                User.builder().email("resepsionis@sentrafarma.com").password(defaultPassword).namaLengkap("Rina Anggraini").role(Role.RESEPSIONIS).noTelepon("081555666777").isDeleted(false).build(),
                User.builder().email("resepsionis2@sentrafarma.com").password(defaultPassword).namaLengkap("Dewi Lestari").role(Role.RESEPSIONIS).noTelepon("081777888999").isDeleted(false).build(),
                
                // 12 Dokter Klinik 24 Jam (Poli Umum, Gigi, KIA, Anak, Emergency)
                User.builder().email("dr.arisandy@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Arisandy Pratama, M.Ked").role(Role.DOKTER).noTelepon("081800112233").isDeleted(false).build(),
                User.builder().email("dr.sabrina@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Sabrina Prameshwari").role(Role.DOKTER).noTelepon("081844556677").isDeleted(false).build(),
                User.builder().email("dr.ghazi@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Ghazi Al-Fatih").role(Role.DOKTER).noTelepon("081888990011").isDeleted(false).build(),
                User.builder().email("drg.mahendra@sentrafarma.com").password(defaultPassword).namaLengkap("drg. Mahendra Varian, Sp.KG").role(Role.DOKTER).noTelepon("081822334455").isDeleted(false).build(),
                User.builder().email("drg.zhafira@sentrafarma.com").password(defaultPassword).namaLengkap("drg. Zhafira Amalia").role(Role.DOKTER).noTelepon("081866778899").isDeleted(false).build(),
                User.builder().email("dr.kalila@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Kalila Kirana, Dip.Derm").role(Role.DOKTER).noTelepon("081899887766").isDeleted(false).build(),
                User.builder().email("dr.naura@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Naura Danendra").role(Role.DOKTER).noTelepon("081877665544").isDeleted(false).build(),
                User.builder().email("dr.aurelia@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Aurelia Savitri, Sp.A").role(Role.DOKTER).noTelepon("081855443322").isDeleted(false).build(),
                User.builder().email("dr.tristan@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Tristan Alamsyah, Sp.A").role(Role.DOKTER).noTelepon("081833221100").isDeleted(false).build(),
                User.builder().email("dr.raditya@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Raditya Mahesa").role(Role.DOKTER).noTelepon("081811223344").isDeleted(false).build(),
                User.builder().email("dr.darren@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Darren Valerius").role(Role.DOKTER).noTelepon("081844332211").isDeleted(false).build(),
                User.builder().email("dr.clarissa@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Clarissa Elvina").role(Role.DOKTER).noTelepon("081866554433").isDeleted(false).build(),
                
                // 20 Pasien
                User.builder().email("pasien1@gmail.com").password(defaultPassword).namaLengkap("Ahmad Dahlan").role(Role.PASIEN).noTelepon("082100010001").isDeleted(false).build(),
                User.builder().email("pasien2@gmail.com").password(defaultPassword).namaLengkap("Budi Gunawan").role(Role.PASIEN).noTelepon("082100010002").isDeleted(false).build(),
                User.builder().email("pasien3@gmail.com").password(defaultPassword).namaLengkap("Cici Paramida").role(Role.PASIEN).noTelepon("082100010003").isDeleted(false).build(),
                User.builder().email("pasien4@gmail.com").password(defaultPassword).namaLengkap("Doni Monardo").role(Role.PASIEN).noTelepon("082100010004").isDeleted(false).build(),
                User.builder().email("pasien5@gmail.com").password(defaultPassword).namaLengkap("Eka Kurniawan").role(Role.PASIEN).noTelepon("082100010005").isDeleted(false).build(),
                User.builder().email("pasien6@gmail.com").password(defaultPassword).namaLengkap("Fani Rahmawati").role(Role.PASIEN).noTelepon("082100010006").isDeleted(false).build(),
                User.builder().email("pasien7@gmail.com").password(defaultPassword).namaLengkap("Gilang Dirga").role(Role.PASIEN).noTelepon("082100010007").isDeleted(false).build(),
                User.builder().email("pasien8@gmail.com").password(defaultPassword).namaLengkap("Hana Pertiwi").role(Role.PASIEN).noTelepon("082100010008").isDeleted(false).build(),
                User.builder().email("pasien9@gmail.com").password(defaultPassword).namaLengkap("Indra Bekti").role(Role.PASIEN).noTelepon("082100010009").isDeleted(false).build(),
                User.builder().email("pasien10@gmail.com").password(defaultPassword).namaLengkap("Joko Widodo").role(Role.PASIEN).noTelepon("082100010010").isDeleted(false).build(),
                User.builder().email("pasien11@gmail.com").password(defaultPassword).namaLengkap("Kania Dewanti").role(Role.PASIEN).noTelepon("082100010011").isDeleted(false).build(),
                User.builder().email("pasien12@gmail.com").password(defaultPassword).namaLengkap("Lukman Sardi").role(Role.PASIEN).noTelepon("082100010012").isDeleted(false).build(),
                User.builder().email("pasien13@gmail.com").password(defaultPassword).namaLengkap("Maya Ahmad").role(Role.PASIEN).noTelepon("082100010013").isDeleted(false).build(),
                User.builder().email("pasien14@gmail.com").password(defaultPassword).namaLengkap("Naufal Rizky").role(Role.PASIEN).noTelepon("082100010014").isDeleted(false).build(),
                User.builder().email("pasien15@gmail.com").password(defaultPassword).namaLengkap("Olivia Jensen").role(Role.PASIEN).noTelepon("082100010015").isDeleted(false).build(),
                User.builder().email("pasien16@gmail.com").password(defaultPassword).namaLengkap("Pradipta Arya").role(Role.PASIEN).noTelepon("082100010016").isDeleted(false).build(),
                User.builder().email("pasien17@gmail.com").password(defaultPassword).namaLengkap("Qiana Naura").role(Role.PASIEN).noTelepon("082100010017").isDeleted(false).build(),
                User.builder().email("pasien18@gmail.com").password(defaultPassword).namaLengkap("Reza Rahadian").role(Role.PASIEN).noTelepon("082100010018").isDeleted(false).build(),
                User.builder().email("pasien19@gmail.com").password(defaultPassword).namaLengkap("Siti Badriah").role(Role.PASIEN).noTelepon("082100010019").isDeleted(false).build(),
                User.builder().email("pasien20@gmail.com").password(defaultPassword).namaLengkap("Taufik Hidayat").role(Role.PASIEN).noTelepon("082100010020").isDeleted(false).build()
        );

        userRepository.saveAll(initialUsers);
        log.info("Auth Service: Berhasil menyimpan {} users ke database PostgreSQL!", initialUsers.size());
    }
}
