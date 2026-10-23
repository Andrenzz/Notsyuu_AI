# Andrea AI

Asisten Android dengan model Gemma 3 1B **sudah dibundel dalam APK**, UI claymorphism, chat pop-up kecil, dan bubble yang dapat digeser.

**[Unduh Andrea AI dari GitHub Releases](https://github.com/Andrenzz/Notsyuu_AI/releases/tag/v0.2.0-preview)**

Pilih **Andrea-AI-0.2.0-preview.apk** pada bagian Assets. Aplikasi masih pratinjau; belum diuji langsung pada Redmi pengguna. Nama repository tetap `Notsyuu_AI` untuk menjaga tautan lama, sedangkan nama aplikasi di HP menjadi **Andrea AI**.

## Perubahan versi 0.2

- Nama dan ikon Andrea AI; package ID dan kunci debug build lokal dipertahankan agar dapat memperbarui pemasangan sebelumnya.
- Model `gemma3-1b-it-int4.litertlm` dari pengguna dibundel tanpa mengubah bobot model. Tidak perlu impor model manual.
- UI claymorphism dengan warna pastel, sudut membulat, serta bayangan lembut pada kartu, tombol, menu, kolom, dan chat.
- Animasi singkat pada perpindahan halaman, tekanan tombol, pesan baru, catatan baru, dialog, progress model, bubble, dan pop-up. Tidak ada animasi berulang ketika siaga. Pengaturan pengurangan animasi sistem dihormati.
- Bubble visual sekitar 43 dp, area sentuh 56 dp, dengan snap ke tepi sekitar 220 ms.
- Saat bubble diseret, tanda × muncul di bawah. Lepaskan bubble pada area × untuk menyembunyikannya.
- Notifikasi tetap menyediakan **Munculkan bubble** ketika bubble disembunyikan. **Matikan asisten** menghentikan layanan dan notifikasinya.
- Pop-up maksimum 320 × 350 dp, dengan area chat yang dapat digulir, mikrofon, Stop, tutup, serta tombol buka aplikasi penuh.
- Izin **Baca layar** dan **Jalankan tindakan** terpisah. Masing-masing memiliki checkbox **Pilih semua**. Izin notifikasi tetap dipilih per aplikasi. Memilih semua tindakan tidak menghilangkan konfirmasi setiap tindakan.

## Pasang dan mulai

1. Unduh APK dari rilis ke HP, lalu pasang melalui pengelola file. Berikan izin pemasangan kepada pengelola file tersebut bila Android memintanya.
2. Buka **Andrea AI**. Pada pembukaan pertama, aplikasi menyalin model bawaan ke penyimpanan internal secara bertahap dan memeriksa SHA-256. Kemajuan tampil di menu Akses. Proses ini hanya diperlukan sekali; sesudahnya model siap dimuat saat ada pertanyaan.
3. Sediakan ruang kosong yang cukup. Model sekitar **584 MB**, APK juga memuat salinannya, dan file unduhan APK masih memakai penyimpanan. Sekitar **2 GB ruang kosong sebelum pemasangan** merupakan perkiraan praktis; kebutuhan sesungguhnya bergantung pada sistem dan file yang masih tersimpan.
4. Buka **Akses**, aktifkan notifikasi, izin tampil di atas aplikasi lain, dan layanan Aksesibilitas jika ingin mengontrol layar.
5. Pada daftar aplikasi, pilih izin baca dan tindakan per aplikasi, atau gunakan **Pilih semua** pada masing-masing kategori. Membuka aplikasi tidak memerlukan izin membaca seluruh isinya.
6. Aktifkan bubble. Ketuk **A** untuk berbicara; jawaban muncul di pop-up kecil di atas aplikasi biasa yang sedang dipakai.

Jika salinan model belum siap atau penyimpanan penuh, gunakan **Akses → Siapkan ulang model**. Kamu tetap dapat mengganti model melalui **Ganti model** dan melepas mesin dari RAM melalui **Lepas dari RAM**.

## Bubble dan notifikasi

- Seret bubble untuk memindahkannya. Setelah dilepas, bubble menempel ke tepi layar dengan animasi singkat.
- Saat diseret, area **×** muncul di bawah layar. Lepaskan di area tersebut untuk menyembunyikan bubble.
- Tarik panel notifikasi dan tekan **Munculkan bubble** untuk memunculkannya lagi.
- Jangan pilih **Matikan asisten** bila hanya ingin menyembunyikan bubble; pilihan tersebut menghentikan layanannya. Untuk menyalakannya kembali setelah dimatikan, buka aplikasi dan aktifkan bubble.
- Notifikasi Android perlu diizinkan. Pembatasan HyperOS, penghentian paksa aplikasi, serta beberapa layar sistem dapat membatasi layanan atau overlay.
- Tekan lama bubble atau gunakan Stop di pop-up untuk menghentikan generasi dan langkah berikutnya. Tindakan yang sudah selesai tidak otomatis dibatalkan.

## Fitur harian

| Kebutuhan | Contoh |
|---|---|
| Tanya jawab | “Jelaskan cara menyusun jadwal belajar.” Model lokal menjawab setelah model siap. |
| Informasi internet | “Cari di internet cara merawat anggrek.” Hasil berupa cuplikan web dan tautan sumber. |
| Membuka aplikasi | “Buka WhatsApp.” Semua aplikasi yang memiliki halaman peluncur bisa dibuka. |
| Membaca layar | Tampilkan aplikasi yang diizinkan, ketuk bubble, lalu “Ringkas layar ini.” |
| Draf pesan | “Buat balasan sopan untuk pesan di layar.” Hasilnya berupa draf di chat. |
| Catatan/tugas | “Catat beli beras”, lalu tandai selesai di Catatan. |
| Pengingat | “Ingatkan 20 menit lagi minum air”, “Ingatkan besok sore ambil paket”. Jadwal selalu dikonfirmasi. |
| Fokus | “Mulai fokus 45 menit.” Jangan Ganggu digunakan bila diizinkan. |
| Rutinitas | Atur jam ringkasan pagi/malam di menu Rutinitas. |
| Kalender | Rutinitas → Lihat kalender; berikan izin kalender bila diperlukan. |
| Cari catatan/file | “Cari catatan kos”; “Cari file laporan” dalam folder pilihan, berdasarkan nama file. |
| Notifikasi | Berikan akses notifikasi Android, pilih aplikasi, lalu tanyakan ringkasannya. Kode autentikasi disaring. |
| Isi formulir | Simpan profil, minta saran isian, pilih kolom tujuan, lalu “Ketik …” dan konfirmasi. |
| Kontrol layar | “Ketuk Cari”, “Gulir ke bawah”, “Ketik tutorial memasak”. Setiap tindakan meminta persetujuan. |
| Beberapa langkah | Awali dengan “Jalankan …”. Rencana maksimum enam langkah; setiap langkah perlu konfirmasi dan dihentikan bila gagal. |

## Baca dan jalankan tindakan

**Baca layar** mengizinkan Andrea AI mengambil teks Aksesibilitas dari aplikasi yang sedang ditampilkan ketika kamu meminta konteks layar. **Jalankan tindakan** mengizinkan ketuk, gulir, dan pengisian kolom pada aplikasi tujuan yang dipilih.

Keduanya tidak memberikan hak root, akses ke direktori privat aplikasi, atau kemampuan melewati sandi dan layar terlindungi. Notifikasi merupakan izin tersendiri. Pilihan semua dapat dicabut melalui checkbox yang sama. Izin kategori lain tidak ikut berubah.

## Suara dan internet

Pengenalan suara mengutamakan layanan on-device Android. Bila tidak tersedia dan mode internet aktif, aplikasi dapat memakai layanan suara HP yang mengirim audio ke penyedianya. Status mendengarkan menandai mode tersebut. Jika internet dimatikan dan pengenalan offline tidak tersedia, gunakan teks.

Jawaban dibacakan memakai suara Indonesia offline yang kamu pilih di Akses. Nada dibuat sedikit rendah dan tenang. Suara pria tertentu belum dibundel; pilih dengan mendengarkan contoh dari mesin TTS HP.

Mode internet dapat dimatikan di Akses. Pencarian mengirim teks pertanyaan ke Bing RSS untuk penggunaan pribadi; konteks layar, profil, notifikasi, dan riwayat tidak otomatis dilampirkan. Pencarian menggunakan maksimal lima cuplikan, bukan membaca seluruh situs. Ketersediaan endpoint publik dan ketepatan hasil tidak dijamin. Model tidak memakai API LLM cloud.

## Model dan lisensi

Model bawaan: **Gemma 3 1B IT int4**, berukuran 584.417.280 byte. SHA-256 dan asal file tercatat pada `MODEL-MANIFEST.json`. Bobot model tidak dimodifikasi.

**Gemma is provided under and subject to the Gemma Terms of Use found at ai.google.dev/gemma/terms.**

Penggunaan dan distribusi model bawaan tunduk pada [Gemma Terms of Use](https://ai.google.dev/gemma/terms), termasuk pembatasan pada Bagian 3.2 dan [Gemma Prohibited Use Policy](https://ai.google.dev/gemma/prohibited_use_policy). Pembatasan tersebut mengatur penggunaan serta distribusi Gemma melalui Andrea AI. Salinan lengkap perjanjian, kebijakan, dan Notice disertakan pada `app/src/main/assets/licenses/` di source dan di dalam APK. Lisensi dapat dilihat lewat Akses → Lisensi Gemma.

Model berada di **aset APK pada GitHub Releases**, bukan riwayat Git, karena ukurannya besar. Source ZIP tidak menyertakan bobot model.

## Mengedit dan membangun source

Buka file di GitHub dan gunakan tombol pensil untuk mengedit, atau clone repository ke Android Studio. Mengedit source di GitHub tidak otomatis mengubah APK yang sudah dirilis.

Persiapan: JDK 17, SDK Android 35. Pulihkan model sebelum build melalui PowerShell:

```powershell
.\tools\prepare-model.ps1 -ModelPath 'C:\lokasi\gemma3-1b-it-int4.litertlm'
```

Jika file model belum ada, jalankan script tanpa parameter. Script mengunduh APK rilis 0.2 dan mengekstrak modelnya, lalu memverifikasi SHA-256. Cara ini memerlukan rilis 0.2 sudah tersedia.

```powershell
.\tools\prepare-model.ps1
.\gradlew.bat assembleDebug lintDebug testDebugUnitTest
```

Jangan commit bobot model, token GitHub, keystore, `local.properties`, atau cache build. APK pembaruan harus ditandatangani dengan kunci yang sama dengan pemasangan lama; kunci tidak disertakan dalam repository.

## Batas dan pemeriksaan

Android minimum 12, ARM64. Build menggunakan kunci debug yang sama dengan pratinjau sebelumnya pada komputer pembuat. Tidak ada jaminan semua versi Android/HyperOS atau semua aplikasi mendukung overlay dan Aksesibilitas.

Pembacaan layar menggunakan teks Aksesibilitas, belum OCR gambar. Kolom sandi, layar kunci, CAPTCHA, dan layar terlindungi tidak didukung. Pengingat menggunakan alarm tidak presisi sehingga dapat terlambat karena penghemat baterai. Formulir belum diisi banyak kolom secara otomatis penuh. Model kecil dapat salah memahami bahasa Indonesia atau rencana tindakan.

Pemeriksaan mencakup build APK, Android lint, tujuh unit test pengingat/geometri bubble, integritas model bundel, tanda tangan APK, dan pengujian antarmuka pada ukuran HP serta pop-up kecil. Kecepatan model, konsumsi baterai, mikrofon, gerakan overlay, notifikasi nyata, dan tindakan lintas aplikasi belum diuji langsung pada HP pengguna.
