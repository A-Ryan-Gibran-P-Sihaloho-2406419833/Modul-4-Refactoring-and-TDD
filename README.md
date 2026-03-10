**Live Deployment URL:** https://modul-2-ci-cd-devops-production-69ee.up.railway.app/  




# Refleksi Tutorial & Latihan Module 2 CI/CD

## 1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

Selama pengerjaan tutorial dan latihan ini,saya memperbaiki beberapa error kualitas kode, yaitu:

* **Konsistensi Penamaan File (Case Sensitivity):**
  Saya mengalami kegagalan pada *pipeline* CI karena perbedaan sistem operasi. Di Windows (lokal), nama file `CreateProduct.html` dianggap sama dengan `createProduct`, tapi di lingkungan Linux (GitHub Actions),hal ini berbeda dan menyebabkan error `TemplateInputException`.
  **Strategi Perbaikan:** Saya menyamakan penamaan file HTML menjadi huruf kecil (camelCase) agar persis dengan *return string* pada Controller (misalnya mengubah `CreateProduct.html` menjadi `createProduct.html`), serta memperbarui *test assertion* agar ekspektasinya sesuai.

* **Kode Tidak Terpakai (Unused Code):**
  Saat menerapkan alat analisis kualitas kode (PMD), saya menemukan adanya variabel yang dideklarasikan tetapi tidak pernah digunakan (*unused private fields*) dan blok `catch` yang kosong. Hal ini dianggap sebagai *bad practice* karena mengotori kode dan berpotensi menyembunyikan error.
  **Strategi Perbaikan:** Saya menghapus variabel sampah tersebut dan membersihkan blok *try-catch* yang tidak diperlukan untuk memastikan kode bersih dan lolos scan PMD. Setelah itu saya lakukan push kembali.

## 2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

Ya, implementasi saat ini sudah memenuhi definisi CI/CD.

1.  **Continuous Integration (CI):** Terpenuhi karena telah membuat workflow GitHub Actions (`ci.yml`, `scorecard.yml`, dan `pmd.yml`) yang secara otomatis menjalankan pengujian (*unit tests*) dan analisis keamanan/kualitas kode setiap kali ada push atau pull request. Ini memastikan kode yang digabungkan selalu teruji dan valid.
2.  **Continuous Deployment (CD):** Terpenuhi melalui integrasi dengan **Koyeb**. Saya telah mengatur agar setiap kali kalau ada perubahan yang berhasil di-merge ke branch `main`, Koyeb akan otomatis menarik kode terbaru, rebuild *Docker image*, dan melakukan deployment aplikasi secara langsung tanpa intervensi manual.  




# Refleksi Modul 3: Maintainability & OO Principles
**Tes Deployment:** https://modul-2-ci-cd-devops-production-69ee.up.railway.app/car/listCar
## 1. Explain what principles you apply to your project!
Pada proyek ini, saya telah menerapkan tiga prinsip SOLID, yaitu:

* **Single Responsibility Principle (SRP):** Sebelumnya, kelas `CarController` ditulis menyatu di dalam file `ProductController.java`. Saya menerapkan SRP dengan memisahkan `CarController` ke dalam file `CarController.java` miliknya sendiri. Kini, masing-masing *controller* hanya memiliki satu tanggung jawab: satu mengurus *routing* Product, dan satu lagi mengurus *routing* Car.
* **Liskov Substitution Principle (LSP):** Sebelumnya, kelas `CarController` dibuat sebagai *subclass* dengan menggunakan `extends ProductController`. Hal ini salah karena rute mobil bukanlah pengganti dari rute produk. Saya menerapkan LSP dengan menghapus `extends` tersebut sehingga `CarController` berdiri sendiri secara independen.
* **Dependency Inversion Principle (DIP):** Pada `CarController`, sebelumnya saya melakukan *inject* (menggunakan `@Autowired`) secara langsung pada *concrete class* yaitu `CarServiceImpl`. Saya memperbaikinya dengan mengubah tipe datanya menjadi *interface* `CarService`. Dengan ini, *controller* (modul tingkat tinggi) bergantung pada abstraksi, bukan pada detail implementasi (modul tingkat rendah).

---

## 2. Explain the advantages of applying SOLID principles to your project with examples.
Menerapkan SOLID membuat kode jauh lebih mudah dirawat (*maintainable*) dan dikembangkan (*scalable*). Berikut contohnya di proyek saya:

* **Lebih Mudah Dicari dan diperbaiki (Efek SRP):** Karena `ProductController` dan `CarController` sudah berada di file yang terpisah, jika suatu saat ada bug pada halaman web mobil, saya tahu persis harus membuka file `CarController.java`. tidak perlu takut secara tidak sengaja merusak kode produk saat sedang memperbaiki kode mobil.
* **Fleksibel terhadap Perubahan (Efek DIP):** Saat ini data mobil hanya disimpan di memori sementara (RAM). Kalau harus mengubahnya untuk menyimpan ke *database* PostgreSQL, saya cukup membuat kelas baru (misalnya `CarServicePostgresImpl` yang meng-implement `CarService`). Saya sama sekali **tidak perlu mengubah** kode di dalam `CarController.java` karena ia hanya bergantung pada *interface*-nya saja.

---

## 3. Explain the disadvantages of not applying SOLID principles to your project with examples.
Kalo prinsip SOLID diabaikan, kode akan menjadi kaku, rapuh, dan sulit untuk dikembangkan seiring membesarnya aplikasi.contohnya:

* **File Membengkak / Spaghetti Code (Pelanggaran SRP):** kalau saya membiarkan `CarController` dan `ProductController` berada di satu file, lama-kelamaan file tersebut akan memiliki ribuan baris kode seiring bertambahnya fitur. Hal ini akan membuat kode sangat pusing untuk dibaca dan dikerjakan bersama tim (rawan terjadi *merge conflict*).
* **Ketergantungan Ketat / Tight Coupling (Pelanggaran DIP):** kalau `CarController` dibiarkan bergantung langsung pada `CarServiceImpl`, maka setiap kali constructor atau struktur dari `CarServiceImpl` berubah, saya terpaksa harus mengedit `CarController` juga. Jika aplikasinya besar dan satu *Service* dipakai oleh banyak *Controller*, saya harus mengedit puluhan file hanya karena satu perubahan kecil di bagian *Service*.



## Modul 4: Refleksi Test-Driven Development (TDD)

**1. Refleksi tentang Alur TDD (Berdasarkan pertanyaan evaluasi Percival)**
Menurut saya, alur kerja TDD (Test-Driven Development) ini terbukti sangat berguna. Dengan menulis tes terlebih dahulu 
(fase RED), saya dipaksa untuk benar-benar memahami apa yang harus dilakukan oleh progrvam sebelum mulai menulis kode aslinya. 
Proses ini membuat p;enulisan kode menjadi lebih terarah (fase GREEN). Selain itu, adanya tes otomatis memberikan rasa aman 
ketika saya harus merapidkan kode (refactoring). Jika perubahan yang sayaa buat merusak fungsi program, tes akan langsung 
gagal dan memberi tahu saya.

Untuk ke depannya, yang perlu saya tingkatkan saat membuat tes adalah memikirkan lebih banyak skenario *edge cases* 
(kasus ekstrem atau jarang terjadi) agar kode saya bisa lebih tangguh menghadapi berbagai kemungkinan input yang tidak terduga.

**2. Refleksi tentang Prinsip F.I.R.S.T dalam Unit Test**
Secara keseluruhan, saya merasa tes yang saya buat di tutorial ini sudahukup baik dan mengikuti prinsip F.I.R.S.T.:
* **Fast (Cepat)**: Tes berjalan sangat cepat dalam hitungan milidetik, sehingga tidak menghambat proses *development*.
* **Independent (Mandiri)**: Setiap tes berdiri sendiri dan tidak bergantung pada tes lainnya. Penggunaan `@BeforeEach` sangat membantu untuk menyiapkan ulang data agar tes tetap terisolasi.
* **Repeatable (Dapat Diulang)**: Tes dapat dijalankan berkali-kali di berbagai *environment* dengan hasil yang konsisten.
* **Self-Validating (Memvalidasi Sendiri)**: Tes otomatis mengecek kebenarannya sendirid menggunakan *assertions* (seperti `assertEquals` atau `assertThrows`) tanpa perlu pengecekan manual.
* **Timely (Tepat Waktu)**: Tes ditulis di waktu yang tepat, yaitu sebelum implementasi kode aslinya dibuat sesuai aturan TDD.

Hal yang mungkin perlu saya perbaiki kea depannya adalah memastikan konsistensi dalam membuat mocking untuk layer seperti Service
agar pengujian benar-benar fokus pada logika bisnis dan tidak bergantung pada komponen di luarnya.