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
                User.builder().email("apoteker@sentrafarma.com").password(defaultPassword).namaLengkap("apt. Bambang Sugianto, S.Farm").role(Role.APOTEKER).noTelepon("081298765432").isDeleted(false).build(),
                User.builder().email("apoteker2@sentrafarma.com").password(defaultPassword).namaLengkap("apt. Siti Nurhaliza, S.Farm").role(Role.APOTEKER).noTelepon("081311223344").isDeleted(false).build(),
                User.builder().email("resepsionis@sentrafarma.com").password(defaultPassword).namaLengkap("Rina Anggraini").role(Role.RESEPSIONIS).noTelepon("081555666777").isDeleted(false).build(),
                User.builder().email("resepsionis2@sentrafarma.com").password(defaultPassword).namaLengkap("Dewi Lestari").role(Role.RESEPSIONIS).noTelepon("081777888999").isDeleted(false).build(),
                User.builder().email("dr.hendra@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Hendra Wijaya, Sp.PD").role(Role.DOKTER).noTelepon("081800112233").isDeleted(false).build(),
                User.builder().email("dr.anisa@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Anisa Rahmawati, Sp.A").role(Role.DOKTER).noTelepon("081844556677").isDeleted(false).build(),
                User.builder().email("dr.budi@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Budi Santoso, Sp.THT").role(Role.DOKTER).noTelepon("081888990011").isDeleted(false).build(),
                User.builder().email("dr.citra@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Citra Kirana, Sp.KK").role(Role.DOKTER).noTelepon("081822334455").isDeleted(false).build(),
                User.builder().email("dr.dian@sentrafarma.com").password(defaultPassword).namaLengkap("dr. Dian Sastro, Sp.OG").role(Role.DOKTER).noTelepon("081866778899").isDeleted(false).build(),
                User.builder().email("pasien1@gmail.com").password(defaultPassword).namaLengkap("Ahmad Dahlan").role(Role.PASIEN).noTelepon("082100010001").isDeleted(false).build(),
                User.builder().email("pasien2@gmail.com").password(defaultPassword).namaLengkap("Budi Gunawan").role(Role.PASIEN).noTelepon("082100010002").isDeleted(false).build(),
                User.builder().email("pasien3@gmail.com").password(defaultPassword).namaLengkap("Cici Paramida").role(Role.PASIEN).noTelepon("082100010003").isDeleted(false).build(),
                User.builder().email("pasien4@gmail.com").password(defaultPassword).namaLengkap("Doni Monardo").role(Role.PASIEN).noTelepon("082100010004").isDeleted(false).build(),
                User.builder().email("pasien5@gmail.com").password(defaultPassword).namaLengkap("Eka Kurniawan").role(Role.PASIEN).noTelepon("082100010005").isDeleted(false).build(),
                User.builder().email("pasien6@gmail.com").password(defaultPassword).namaLengkap("Fani Rahmawati").role(Role.PASIEN).noTelepon("082100010006").isDeleted(false).build(),
                User.builder().email("pasien7@gmail.com").password(defaultPassword).namaLengkap("Gilang Dirga").role(Role.PASIEN).noTelepon("082100010007").isDeleted(false).build(),
                User.builder().email("pasien8@gmail.com").password(defaultPassword).namaLengkap("Hana Pertiwi").role(Role.PASIEN).noTelepon("082100010008").isDeleted(false).build(),
                User.builder().email("pasien9@gmail.com").password(defaultPassword).namaLengkap("Indra Bekti").role(Role.PASIEN).noTelepon("082100010009").isDeleted(false).build(),
                User.builder().email("pasien10@gmail.com").password(defaultPassword).namaLengkap("Joko Widodo").role(Role.PASIEN).noTelepon("082100010010").isDeleted(false).build()
        );

        userRepository.saveAll(initialUsers);
        log.info("Auth Service: Berhasil menyimpan {} users ke database PostgreSQL!", initialUsers.size());
    }
}
