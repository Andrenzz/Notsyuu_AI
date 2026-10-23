# Konfigurasi GitHub MCP untuk Codex

File `github-mcp-config.toml` adalah potongan konfigurasi, bukan pengganti seluruh konfigurasi Codex.

1. Di Codex, buka pengaturan MCP dan konfigurasi `config.toml`.
2. Gabungkan isi file `github-mcp-config.toml`. Jika sudah ada bagian `[mcp_servers.github]`, edit bagian tersebut; jangan membuat bagian bernama sama dua kali.
3. Buat atau gunakan personal access token GitHub dengan akses hanya ke repository yang diperlukan. Untuk menulis file dan mengunggah aset rilis pada repository yang sudah ada, gunakan izin **Contents: Read and write**. Membuat repository baru mungkin memerlukan izin tambahan sesuai jenis token dan akun.
4. Simpan token sebagai environment variable pengguna Windows bernama `GITHUB_PAT_TOKEN`. Cara tanpa menuliskan token dalam riwayat perintah: buka pengaturan Windows **Edit environment variables for your account**, pilih **New**, isi nama tersebut, dan masukkan token pada kolom nilai. Jangan masukkan token ke source code atau chat.
5. Tutup seluruh proses Codex lalu buka kembali agar environment variable terbaca. Periksa status koneksi GitHub pada pengaturan MCP.

Konfigurasi ini belum memasang token atau mengunggah APK. Mengunggah aset APK ke GitHub Releases juga memerlukan kemampuan upload aset dari alat GitHub yang digunakan; koneksi MCP saja tidak menjamin fitur upload binary tersedia.

Rujukan konfigurasi: [OpenAI Docs — MCP](https://developers.openai.com/codex/mcp).
