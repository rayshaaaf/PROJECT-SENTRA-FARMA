package com.sentrafarma.pharmacy.config;

import com.sentrafarma.pharmacy.entity.KategoriObat;
import com.sentrafarma.pharmacy.entity.Obat;
import com.sentrafarma.pharmacy.repository.KategoriObatRepository;
import com.sentrafarma.pharmacy.repository.ObatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final KategoriObatRepository kategoriRepository;
    private final ObatRepository obatRepository;

    public DataSeeder(KategoriObatRepository kategoriRepository, ObatRepository obatRepository) {
        this.kategoriRepository = kategoriRepository;
        this.obatRepository = obatRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (obatRepository.count() >= 50) {
            log.info("Pharmacy Service: Database sudah terisi {} item. Seeding dilewati.", obatRepository.count());
            return;
        }

        log.info("Pharmacy Service: Seeding 50 Item Inventory Obat ke Database...");

        // 1. Kategori
        KategoriObat katAnalgesik = getOrCreateKategori("Analgesik & Antipiretik", "Obat pereda nyeri dan penurun demam");
        KategoriObat katAntibiotik = getOrCreateKategori("Antibiotik", "Obat untuk infeksi bakteri");
        KategoriObat katVitamin = getOrCreateKategori("Vitamin & Suplemen", "Suplemen kesehatan daya tahan tubuh");
        KategoriObat katAntihistamin = getOrCreateKategori("Antihistamin & Alergi", "Obat anti alergi dan gatal");
        KategoriObat katObatBatuk = getOrCreateKategori("Obat Batuk & Flu", "Obat pereda gejala batuk, flu, dan pilek");
        KategoriObat katLambung = getOrCreateKategori("Obat Asam Lambung & Maag", "Obat penetral asam lambung & tukak maag");
        KategoriObat katHipertensi = getOrCreateKategori("Hipertensi & Jantung", "Obat pengontrol tekanan darah tinggi");
        KategoriObat katDiabetes = getOrCreateKategori("Diabetes & Metabolisme", "Obat penurun kadar gula darah");
        KategoriObat katMataKulit = getOrCreateKategori("Obat Mata & Kulit", "Tetes mata steril & salep kulit peradangan");

        // 2. 50 Item Obat Realistis
        List<Obat> initialObat = List.of(
                // 1-4: Analgesik
                Obat.builder().kategori(katAnalgesik).kodeObat("OBT-001").namaObat("Paracetamol 500mg").satuan("Strip").harga(8500.0).stok(150).minStok(20).tanggalKadaluarsa("2028-12-31").isDeleted(false).build(),
                Obat.builder().kategori(katAnalgesik).kodeObat("OBT-002").namaObat("Ibuprofen 400mg").satuan("Strip").harga(12000.0).stok(80).minStok(15).tanggalKadaluarsa("2027-10-15").isDeleted(false).build(),
                Obat.builder().kategori(katAnalgesik).kodeObat("OBT-003").namaObat("Asam Mefenamat 500mg").satuan("Strip").harga(15000.0).stok(5).minStok(10).tanggalKadaluarsa("2027-05-20").isDeleted(false).build(),
                Obat.builder().kategori(katAnalgesik).kodeObat("OBT-004").namaObat("Natrium Diklofenak 50mg").satuan("Strip").harga(18000.0).stok(60).minStok(10).tanggalKadaluarsa("2028-01-10").isDeleted(false).build(),
                
                // 5-8: Antibiotik
                Obat.builder().kategori(katAntibiotik).kodeObat("OBT-005").namaObat("Amoxicillin 500mg").satuan("Strip").harga(22000.0).stok(90).minStok(20).tanggalKadaluarsa("2027-08-30").isDeleted(false).build(),
                Obat.builder().kategori(katAntibiotik).kodeObat("OBT-006").namaObat("Cefadroxil 500mg").satuan("Strip").harga(35000.0).stok(40).minStok(10).tanggalKadaluarsa("2027-11-12").isDeleted(false).build(),
                Obat.builder().kategori(katAntibiotik).kodeObat("OBT-007").namaObat("Azithromycin 500mg").satuan("Strip").harga(65000.0).stok(30).minStok(5).tanggalKadaluarsa("2028-03-25").isDeleted(false).build(),
                Obat.builder().kategori(katAntibiotik).kodeObat("OBT-008").namaObat("Ciprofloxacin 500mg").satuan("Strip").harga(28000.0).stok(50).minStok(10).tanggalKadaluarsa("2027-09-01").isDeleted(false).build(),
                
                // 9-12: Vitamin
                Obat.builder().kategori(katVitamin).kodeObat("OBT-009").namaObat("Vitamin C 1000mg Enervon-C").satuan("Botol").harga(45000.0).stok(200).minStok(30).tanggalKadaluarsa("2029-06-30").isDeleted(false).build(),
                Obat.builder().kategori(katVitamin).kodeObat("OBT-010").namaObat("Vitamin D3 1000 IU Proof").satuan("Botol").harga(85000.0).stok(110).minStok(15).tanggalKadaluarsa("2029-01-15").isDeleted(false).build(),
                Obat.builder().kategori(katVitamin).kodeObat("OBT-011").namaObat("Neurobion Forte Pink").satuan("Strip").harga(38000.0).stok(75).minStok(15).tanggalKadaluarsa("2028-07-20").isDeleted(false).build(),
                Obat.builder().kategori(katVitamin).kodeObat("OBT-012").namaObat("Sangobion Kapsul Penambah Darah").satuan("Strip").harga(24000.0).stok(3).minStok(10).tanggalKadaluarsa("2027-12-01").isDeleted(false).build(),
                
                // 13-15: Antihistamin
                Obat.builder().kategori(katAntihistamin).kodeObat("OBT-013").namaObat("Cetirizine 10mg").satuan("Strip").harga(10000.0).stok(130).minStok(20).tanggalKadaluarsa("2028-04-18").isDeleted(false).build(),
                Obat.builder().kategori(katAntihistamin).kodeObat("OBT-014").namaObat("CTM 4mg Anti Alergi").satuan("Strip").harga(5000.0).stok(300).minStok(50).tanggalKadaluarsa("2029-10-10").isDeleted(false).build(),
                Obat.builder().kategori(katAntihistamin).kodeObat("OBT-015").namaObat("Loratadine 10mg").satuan("Strip").harga(14000.0).stok(85).minStok(15).tanggalKadaluarsa("2028-02-14").isDeleted(false).build(),
                
                // 16-20: Obat Batuk & Flu
                Obat.builder().kategori(katObatBatuk).kodeObat("OBT-016").namaObat("OBH Combi Batuk Flu 100ml").satuan("Botol").harga(23500.0).stok(95).minStok(15).tanggalKadaluarsa("2028-05-30").isDeleted(false).build(),
                Obat.builder().kategori(katObatBatuk).kodeObat("OBT-017").namaObat("Siladex DMP Batuk Kering 60ml").satuan("Botol").harga(19500.0).stok(70).minStok(10).tanggalKadaluarsa("2028-08-20").isDeleted(false).build(),
                Obat.builder().kategori(katObatBatuk).kodeObat("OBT-018").namaObat("Ambroxol Syrup 15mg/5ml").satuan("Botol").harga(14500.0).stok(65).minStok(10).tanggalKadaluarsa("2027-07-11").isDeleted(false).build(),
                Obat.builder().kategori(katObatBatuk).kodeObat("OBT-019").namaObat("Laserin Obat Batuk Herbal 110ml").satuan("Botol").harga(27000.0).stok(40).minStok(10).tanggalKadaluarsa("2028-09-09").isDeleted(false).build(),
                Obat.builder().kategori(katObatBatuk).kodeObat("OBT-020").namaObat("Bisolvon Kids Syrup 60ml").satuan("Botol").harga(42000.0).stok(50).minStok(10).tanggalKadaluarsa("2028-03-03").isDeleted(false).build(),

                // 21-26: Asam Lambung & Maag
                Obat.builder().kategori(katLambung).kodeObat("OBT-021").namaObat("Promag Tablet Kunyah").satuan("Strip").harga(10500.0).stok(140).minStok(20).tanggalKadaluarsa("2028-11-20").isDeleted(false).build(),
                Obat.builder().kategori(katLambung).kodeObat("OBT-022").namaObat("Mylanta Cair 150ml").satuan("Botol").harga(48000.0).stok(60).minStok(10).tanggalKadaluarsa("2028-06-15").isDeleted(false).build(),
                Obat.builder().kategori(katLambung).kodeObat("OBT-023").namaObat("Omeprazole 20mg Kapsul").satuan("Strip").harga(16000.0).stok(110).minStok(15).tanggalKadaluarsa("2027-09-30").isDeleted(false).build(),
                Obat.builder().kategori(katLambung).kodeObat("OBT-024").namaObat("Lansoprazole 30mg Kapsul").satuan("Strip").harga(21000.0).stok(80).minStok(10).tanggalKadaluarsa("2028-04-12").isDeleted(false).build(),
                Obat.builder().kategori(katLambung).kodeObat("OBT-025").namaObat("Sucralfate Suspensi 500mg/5ml").satuan("Botol").harga(32000.0).stok(45).minStok(10).tanggalKadaluarsa("2028-01-25").isDeleted(false).build(),
                Obat.builder().kategori(katLambung).kodeObat("OBT-026").namaObat("Polysilane Sirup 100ml").satuan("Botol").harga(28500.0).stok(70).minStok(10).tanggalKadaluarsa("2028-08-10").isDeleted(false).build(),

                // 27-32: Hipertensi & Jantung
                Obat.builder().kategori(katHipertensi).kodeObat("OBT-027").namaObat("Amlodipine 5mg").satuan("Strip").harga(9000.0).stok(160).minStok(25).tanggalKadaluarsa("2028-10-05").isDeleted(false).build(),
                Obat.builder().kategori(katHipertensi).kodeObat("OBT-028").namaObat("Amlodipine 10mg").satuan("Strip").harga(14000.0).stok(120).minStok(20).tanggalKadaluarsa("2028-12-12").isDeleted(false).build(),
                Obat.builder().kategori(katHipertensi).kodeObat("OBT-029").namaObat("Captopril 25mg").satuan("Strip").harga(7500.0).stok(180).minStok(30).tanggalKadaluarsa("2027-11-18").isDeleted(false).build(),
                Obat.builder().kategori(katHipertensi).kodeObat("OBT-030").namaObat("Candesartan 8mg").satuan("Strip").harga(38000.0).stok(65).minStok(10).tanggalKadaluarsa("2028-05-14").isDeleted(false).build(),
                Obat.builder().kategori(katHipertensi).kodeObat("OBT-031").namaObat("Bisoprolol 5mg").satuan("Strip").harga(32000.0).stok(75).minStok(10).tanggalKadaluarsa("2028-07-22").isDeleted(false).build(),
                Obat.builder().kategori(katHipertensi).kodeObat("OBT-032").namaObat("Valsartan 80mg").satuan("Strip").harga(55000.0).stok(40).minStok(5).tanggalKadaluarsa("2028-02-08").isDeleted(false).build(),

                // 33-38: Diabetes & Metabolisme
                Obat.builder().kategori(katDiabetes).kodeObat("OBT-033").namaObat("Metformin 500mg").satuan("Strip").harga(8000.0).stok(200).minStok(30).tanggalKadaluarsa("2028-09-15").isDeleted(false).build(),
                Obat.builder().kategori(katDiabetes).kodeObat("OBT-034").namaObat("Glimepiride 2mg").satuan("Strip").harga(18000.0).stok(95).minStok(15).tanggalKadaluarsa("2028-06-20").isDeleted(false).build(),
                Obat.builder().kategori(katDiabetes).kodeObat("OBT-035").namaObat("Glibenclamide 5mg").satuan("Strip").harga(6500.0).stok(150).minStok(20).tanggalKadaluarsa("2027-10-30").isDeleted(false).build(),
                Obat.builder().kategori(katDiabetes).kodeObat("OBT-036").namaObat("Simvastatin 10mg Kolesterol").satuan("Strip").harga(11000.0).stok(130).minStok(20).tanggalKadaluarsa("2028-08-05").isDeleted(false).build(),
                Obat.builder().kategori(katDiabetes).kodeObat("OBT-037").namaObat("Atorvastatin 20mg").satuan("Strip").harga(45000.0).stok(70).minStok(10).tanggalKadaluarsa("2028-04-19").isDeleted(false).build(),
                Obat.builder().kategori(katDiabetes).kodeObat("OBT-038").namaObat("Allopurinol 100mg Asam Urat").satuan("Strip").harga(10000.0).stok(160).minStok(25).tanggalKadaluarsa("2028-11-11").isDeleted(false).build(),

                // 39-44: Mata & Kulit
                Obat.builder().kategori(katMataKulit).kodeObat("OBT-039").namaObat("Insto Regular Tetes Mata 7.5ml").satuan("Botol").harga(16500.0).stok(100).minStok(15).tanggalKadaluarsa("2028-12-01").isDeleted(false).build(),
                Obat.builder().kategori(katMataKulit).kodeObat("OBT-040").namaObat("Braito Steril Tetes Mata 5ml").satuan("Botol").harga(14000.0).stok(85).minStok(10).tanggalKadaluarsa("2028-07-15").isDeleted(false).build(),
                Obat.builder().kategori(katMataKulit).kodeObat("OBT-041").namaObat("Bioplacenton Salep Luka Bakar 15g").satuan("Tube").harga(31000.0).stok(60).minStok(10).tanggalKadaluarsa("2028-09-28").isDeleted(false).build(),
                Obat.builder().kategori(katMataKulit).kodeObat("OBT-042").namaObat("Hydrocortisone 1% Cream 5g").satuan("Tube").harga(12500.0).stok(75).minStok(10).tanggalKadaluarsa("2028-03-10").isDeleted(false).build(),
                Obat.builder().kategori(katMataKulit).kodeObat("OBT-043").namaObat("Salep 88 Kurap & Jamur 6g").satuan("Pot").harga(13500.0).stok(90).minStok(15).tanggalKadaluarsa("2029-01-08").isDeleted(false).build(),
                Obat.builder().kategori(katMataKulit).kodeObat("OBT-044").namaObat("Betadine Antiseptic Ointment 10g").satuan("Tube").harga(26000.0).stok(65).minStok(10).tanggalKadaluarsa("2028-10-22").isDeleted(false).build(),

                // 45-50: Suplemen Tambahan & Lainnya
                Obat.builder().kategori(katVitamin).kodeObat("OBT-045").namaObat("Imboost Force Tablet Kaplet").satuan("Strip").harga(42000.0).stok(85).minStok(15).tanggalKadaluarsa("2028-11-30").isDeleted(false).build(),
                Obat.builder().kategori(katVitamin).kodeObat("OBT-046").namaObat("Fitkom Gummy Anak Rasa Buah").satuan("Botol").harga(22000.0).stok(110).minStok(15).tanggalKadaluarsa("2028-08-14").isDeleted(false).build(),
                Obat.builder().kategori(katAnalgesik).kodeObat("OBT-047").namaObat("Bodrex Extra Sakit Kepala").satuan("Strip").harga(6000.0).stok(250).minStok(40).tanggalKadaluarsa("2029-05-10").isDeleted(false).build(),
                Obat.builder().kategori(katObatBatuk).kodeObat("OBT-048").namaObat("Komix Herbal Tube Batuk 15ml").satuan("Dus").harga(18500.0).stok(120).minStok(20).tanggalKadaluarsa("2028-09-01").isDeleted(false).build(),
                Obat.builder().kategori(katLambung).kodeObat("OBT-049").namaObat("Inpepsa Sucralfate 100ml").satuan("Botol").harga(68000.0).stok(40).minStok(5).tanggalKadaluarsa("2028-04-20").isDeleted(false).build(),
                Obat.builder().kategori(katVitamin).kodeObat("OBT-050").namaObat("Curcuma Plus Emulsion 200ml").satuan("Botol").harga(34000.0).stok(75).minStok(10).tanggalKadaluarsa("2028-07-07").isDeleted(false).build()
        );

        // Delete existing items if re-seeding to ensure fresh 50 items
        obatRepository.deleteAll();
        obatRepository.saveAll(initialObat);
        log.info("Pharmacy Service: BERHASIL menyimpan {} item obat ke database!", initialObat.size());
    }

    private KategoriObat getOrCreateKategori(String nama, String deskripsi) {
        return kategoriRepository.findAll().stream()
                .filter(k -> k.getNamaKategori().equalsIgnoreCase(nama))
                .findFirst()
                .orElseGet(() -> kategoriRepository.save(KategoriObat.builder().namaKategori(nama).deskripsi(deskripsi).build()));
    }
}
