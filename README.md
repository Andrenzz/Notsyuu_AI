# Notsyuu AI

Asisten Android dengan AI lokal, tampilan glass, tombol mengambang, chat pop-up, pencarian internet opsional, catatan, pengingat, dan rutinitas.

**[Unduh APK untuk HP melalui GitHub Releases](https://github.com/Andrenzz/Notsyuu_AI/releases/tag/v0.1.0-preview)**

Di halaman rilis, buka bagian **Assets** lalu pilih **Notsyuu-0.1.0-preview.apk**. Model AI diimpor terpisah; petunjuk tersedia di bawah. Aplikasi masih versi pratinjau dan belum diuji langsung pada perangkat pengguna.

Source Android berada di folder `app/`. Untuk mengedit melalui GitHub, buka file lalu pilih tombol pensil, atau buka repository di Android Studio untuk pengembangan dan build. Perubahan source tidak otomatis mengubah APK pada rilis yang sudah diterbitkan.

---

# Notsyuu — panduan versi pratinjau

Aplikasi Android lokal dengan tampilan glass gelap, dibuat untuk dicoba pada Redmi Note 13 Pro 5G RAM 8 GB. Versi 0.1.0-preview; Android 12 atau lebih baru, ARM64. APK ditandatangani dengan kunci debug untuk pemasangan percobaan, bukan rilis Play Store.

## Pasang dan mulai

1. Salin **Notsyuu-0.1.0-preview.apk** ke HP, buka file tersebut, lalu izinkan pemasangan dari aplikasi pengelola file yang kamu gunakan bila diminta Android.
2. Buka Notsyuu → **Akses**. Aktifkan **notifikasi pengingat** agar pengingat bisa muncul.
3. Untuk kontrol HP, aktifkan layanan **Notsyuu** dalam pengaturan Aksesibilitas. Pada daftar aplikasi di Notsyuu, centang **Layar** hanya untuk aplikasi yang ingin dibaca dan dikendalikan.
4. Tekan **Aktifkan** pada tombol mengambang dan berikan izin tampil di atas aplikasi lain. Tombol N bisa digeser ke tepi layar. Tekan lama untuk Stop; sembunyikan lewat notifikasi atau menu Akses.
5. Semua aplikasi dengan halaman peluncur bisa dibuka melalui “Buka [nama aplikasi]”, tanpa perlu mencentang izin membaca layarnya.

## Memasang model AI lokal

Model tidak dibundel dalam APK. Tanpa model, membuka aplikasi, catatan, pencarian nama file, timer fokus, pengingat, dan rutinitas tetap tersedia. Percakapan bebas, ringkasan, draf balasan, dan perencanaan langkah memerlukan model.

1. Buka [halaman Gemma 3 1B untuk LiteRT](https://huggingface.co/litert-community/Gemma3-1B-IT). Halaman ini mengharuskan login serta meninjau dan menyetujui lisensi Gemma sebelum file bisa diunduh.
2. Pada daftar file, pilih **gemma3-1b-it-int4.litertlm** untuk CPU/GPU. Hindari varian NPU khusus Tensor atau chipset lain, file `.task`, `.tflite`, dan GGUF. Aplikasi ini menggunakan backend CPU.
3. Simpan file di HP, lalu buka Notsyuu → Akses → **Impor model**. Batas impor 2,2 GB. Sediakan ruang kosong untuk file unduhan sekaligus salinannya di penyimpanan aplikasi.
4. Coba perintah singkat, misalnya “Buat daftar belanja sederhana”. Memuat model pertama kali memerlukan waktu. File yang berhasil diimpor belum berarti sudah terbukti kompatibel; validasi mesin berlangsung saat inferensi pertama.
5. Gunakan **Lepas dari RAM** setelah selesai jika ingin mengosongkan memori model.

Model kecil membatasi kualitas bahasa Indonesia, penalaran, dan ketepatan rencana. Tidak ada API LLM cloud atau akun Notsyuu. APK meminta izin internet untuk pencarian web opsional; pemahaman dan penyusunan jawaban AI tetap memakai model lokal. Aplikasi lain yang dibuka memakai koneksi mereka sendiri. Pemrosesan model pada Redmi ini belum diuji langsung.

## Suara pria dan tombol bicara

Di Akses → **Pilih & coba suara**, pilih suara Indonesia offline yang terdengar paling sesuai. Notsyuu memakai nada sedikit rendah dan tempo tenang. Android tidak menyediakan label gender yang konsisten, sehingga suara pria tertentu tidak dapat dijamin atau dibuat hanya dengan menurunkan nada. Jika tidak ada suara yang cocok, perlu memasang mesin/data suara offline tambahan.

Pengenalan ucapan mengutamakan layanan **on-device** Android. Jika tidak tersedia, mode internet mengizinkan layanan pengenalan suara bawaan HP yang dapat mengirim audio ke penyedianya. Status mendengarkan menandai mode tersebut. Matikan internet di Akses untuk membatasi ke pengenalan offline; bila layanan offline tidak tersedia, gunakan teks. Ketukan tombol N membuka chat pop-up di atas aplikasi yang sedang dipakai dan mulai mendengarkan. Jawaban muncul di pop-up yang sama; tombol × menutupnya dan ↗ membuka aplikasi penuh. Mikrofon hanya aktif setelah tombol bicara ditekan; tidak ada pendengaran kata panggil sepanjang hari.

## Contoh tugas

| Kebutuhan | Cara menggunakan |
|---|---|
| Tanya jawab umum | Tanyakan lewat chat atau mikrofon. Model lokal menjawab pertanyaan, memberi penjelasan, dan membantu menyusun tulisan. |
| Informasi internet | Aktifkan internet di Akses, lalu “Cari di internet [topik]”. Pertanyaan yang diawali apa, siapa, kapan, berapa, mengapa, bagaimana, atau jelaskan juga dapat memakai pencarian. Sumber ditampilkan sebagai tautan. |
| Buka aplikasi | “Buka WhatsApp” atau “Buka YouTube”. |
| Catatan dan tugas | “Catat beli beras”, lalu tandai selesai di Catatan. |
| Cari catatan | “Cari catatan pembayaran”. |
| Pengingat bahasa sehari-hari | “Ingatkan saya besok sore ambil paket” atau “Ingatkan 20 menit lagi minum air”. Periksa tanggal/jam sebelum menyimpan. |
| Tanggal yang belum dipahami | Aplikasi membuka pemilih tanggal dan jam, lalu meminta konfirmasi. |
| Fokus | “Mulai fokus 45 menit”. Beri izin Jangan Ganggu jika diinginkan. Membatalkan timer fokus memulihkan pengaturan sebelumnya. |
| Rutinitas pagi/malam | Pilih jam di Rutinitas. Ringkasan tugas dan pengingat dikirim sebagai notifikasi, tanpa menyalakan model. |
| Kalender | Rutinitas → Lihat kalender. Berikan izin kalender, lalu tekan lagi setelah izin diberikan. |
| Ringkas layar | Tampilkan aplikasi yang sudah diizinkan, ketuk tombol N, lalu ucapkan perintah. Jika suara offline belum tersedia, ketik perintah setelah tombol membuka Notsyuu. Teks layar ditangkap saat tombol N ditekan. |
| Draf balasan | Tampilkan pesan, panggil lewat N, lalu minta “Buat balasan sopan untuk pesan di layar”. Draf ditampilkan di chat. |
| Isi kolom | Pilih kolom pada aplikasi tujuan. Panggil Notsyuu lalu “Ketik [isi]”. Periksa aplikasi tujuan dan setujui tindakan. Kolom sandi dilewati. |
| Ketuk dan gulir | “Ketuk Cari”, “Gulir ke bawah”. Persetujuan diperlukan sebelum setiap tindakan. |
| Beberapa langkah | “Jalankan buka YouTube lalu cari tutorial memasak”. Model membuat rencana maksimum 6 langkah, lalu pengguna menyetujui setiap langkah. Rencana dihentikan bila tindakan gagal. |
| Pengingat pesan | “Ingatkan dalam 30 menit membalas pesan dari Budi”. Tambahkan isi singkat yang ingin diingatkan. |
| Cari file | Pilih folder di Akses, lalu “Cari file laporan”. Yang dicari adalah nama file, bukan isinya. Batas 2.000 entri, kedalaman 8 folder. |
| Ringkas notifikasi | Beri akses notifikasi Android dan centang Notifikasi pada aplikasi pilihan. Notifikasi baru setelah izin aktif dapat diringkas dengan model. Pesan yang tampak seperti kode verifikasi disaring. |
| Formulir | Simpan profil di Akses. Minta saran isian dari layar, pilih kolom, lalu gunakan perintah “Ketik …” dan konfirmasi. Pengisian banyak kolom belum otomatis penuh. |

## Batas versi ini

- Ini APK pratinjau yang berhasil dikompilasi, bukan klaim seluruh fitur sudah diuji pada Redmi. Belum ada perangkat fisik yang tersambung saat pembuatan.
- Pembacaan memakai **teks Aksesibilitas**, belum mencakup OCR tangkapan layar. Aplikasi yang tidak mengekspos teks, layar terlindungi, layar kunci, CAPTCHA, serta kolom sandi tidak didukung.
- Otomatisasi dilakukan melalui elemen yang tersedia dan konfirmasi per langkah. Model dapat salah merencanakan nama tombol; konfirmasi tidak menjamin rencananya benar. Tidak ada kendali tanpa batas terhadap sistem Android.
- Pengingat menggunakan alarm tidak presisi agar hemat daya. Android/HyperOS dapat menunda notifikasi; aplikasi ini tidak ditujukan untuk alarm dengan ketepatan kritis.
- Rutinitas pagi/malam merangkum catatan dan pengingat. Integrasi kalender tersedia lewat tombol tersendiri; belum masuk otomatis ke ringkasan rutin.
- Kualitas suara pria dan pengenalan ucapan bergantung pada layanan suara yang terpasang. Suara pria khusus belum dibundel.
- Konsumsi baterai dan kecepatan model belum diukur di HP pengguna. Tombol mengambang tidak terus menyalakan mikrofon atau mengambil gambar layar.
- Stop meminta pembatalan generasi dan membatalkan langkah berikutnya; tindakan yang sudah selesai tidak otomatis dibatalkan.

## Data dan akses

Chat, catatan, profil, riwayat, dan ringkasan notifikasi disimpan dalam ruang aplikasi di HP. Pilih izin layar dan notifikasi per aplikasi. Jangan menyimpan PIN, sandi, OTP, atau data kartu di profil. Hapus chat/riwayat melalui Akses, atau hapus data aplikasi lewat pengaturan Android untuk menghapus seluruh data termasuk model. Model hanya menerima konteks yang diperlukan oleh perintah; profil disertakan untuk permintaan formulir. Ketika mode internet aktif, pertanyaan pengetahuan dapat dikirim ke pencarian Bing RSS melalui HTTPS. Isi layar, profil, riwayat percakapan, dan notifikasi tidak otomatis dilampirkan ke pencarian. Jika layanan suara daring dipakai, audio diproses oleh penyedia layanan suara HP sesuai pengaturannya.

## Internet dan pop-up

Mode internet aktif secara bawaan sesuai permintaan pengguna, dan bisa dimatikan di Akses. Pencarian menggunakan cuplikan hasil Bing RSS untuk penggunaan pribadi. Hasil maksimal lima sumber dengan tautan HTTPS; tanpa akun atau kunci API pencarian. Ketersediaan endpoint publik dan kualitas hasil tidak dijamin. Model tidak membaca seluruh isi situs, tidak menonton video, dan tidak mengakses halaman yang memerlukan login. Hasil bukan jaminan fakta terbaru; buka sumber bila membutuhkan kepastian. Tanpa model, aplikasi menampilkan hasil pencarian langsung dan menjelaskan bahwa hasil belum dirangkum oleh AI.

Pop-up merupakan jendela dialog Android dari tombol mengambang, dengan chat, mikrofon, tombol Stop, tutup, dan buka aplikasi penuh. Dapat digunakan di atas aplikasi biasa; layar kunci, beberapa layar keamanan, dan pembatasan sistem bisa menghalangi tampilannya. Tidak mencakup pendengaran suara sepanjang hari.

## Pemeriksaan yang dilakukan

- Build APK Android berhasil; tanda tangan APK diverifikasi.
- Android lint: tidak ada error; beberapa peringatan kompatibilitas/gaya dan analisis sumber daya tetap dicatat dalam laporan build.
- Empat pengujian pengingat: melewati tengah malam, besok sore, jam malam eksplisit, serta waktu ambigu/tidak valid.
- Pemeriksaan tampilan lebar 393 piksel: lima halaman, tanpa luapan horizontal, menambah/menyelesaikan catatan, mengirim chat pratinjau, dialog konfirmasi, dan penanganan teks HTML sebagai teks biasa.
- Belum diuji di perangkat: inferensi model, izin HyperOS, suara, overlay, pengiriman alarm nyata, dan tindakan Aksesibilitas lintas aplikasi.

## Membangun dari source

Buka folder proyek Notsyuu di Android Studio dengan JDK 17 dan SDK Android 35, atau jalankan `gradlew.bat assembleDebug lintDebug testDebugUnitTest` dari folder proyek. Runtime AI: [LiteRT-LM Android](https://developers.google.com/edge/litert-lm/android). Gradle wrapper disertakan; unduhan dependency dibutuhkan saat build pertama. File `local.properties` harus menunjuk SDK pada komputer pengembang sendiri.
