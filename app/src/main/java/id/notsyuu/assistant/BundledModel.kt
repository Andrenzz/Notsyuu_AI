package id.notsyuu.assistant

import android.content.Context
import java.io.File
import java.security.MessageDigest

/** Copies the bundled model in chunks, once, without allocating the model in RAM. */
object BundledModel {
    const val ASSET = "models/gemma3-1b-it-int4.litertlm"
    const val SIZE = 584417280L
    // Filled at build preparation from the exact model supplied by the user.
    const val SHA256 = "1325ae366d31950f137c9c357b9fa89448b176d76998180c08ceaca78bba98be"
    @Volatile var preparing = false
        private set
    @Volatile var progress = 0
        private set
    private val lock = Any()
    fun file(c: Context): File {
        val imported=File(c.filesDir,"model.litertlm")
        return if(imported.exists()) imported else File(c.filesDir,"gemma3-bundled.litertlm")
    }
    fun ready(c: Context):Boolean { val f=file(c);return f.exists() && if(f.name=="model.litertlm") f.length()>1024 else f.length()==SIZE }
    fun ensure(c: Context,onProgress: (Int)->Unit = {}): File = synchronized(lock) {
        val target=file(c)
        if(target.exists() && (target.name=="model.litertlm" || target.length()==SIZE)) return@synchronized target
        preparing=true; progress=0
        val temp=File(c.filesDir,"gemma3-bundled.part")
        try {
            require(c.filesDir.usableSpace>SIZE+128*1024*1024) { "Ruang kosong belum cukup untuk menyiapkan Gemma. Sediakan setidaknya 750 MB lagi." }
            val digest=MessageDigest.getInstance("SHA-256")
            c.assets.open(ASSET).use { input -> temp.outputStream().use { out ->
                val buffer=ByteArray(1024*1024); var total=0L; var last=-1
                while(true) { val n=input.read(buffer); if(n<0)break; out.write(buffer,0,n);digest.update(buffer,0,n);total+=n
                    val percent=(total*100/SIZE).toInt().coerceAtMost(100)
                    if(percent!=last) { progress=percent;onProgress(percent);last=percent }
                }
            } }
            require(temp.length()==SIZE) { "Ukuran model bawaan tidak sesuai." }
            val hash=digest.digest().joinToString("") { "%02x".format(it) }
            require(hash==SHA256) { "Pemeriksaan integritas model gagal. Unduh ulang APK." }
            check(temp.renameTo(target)) { "Model belum dapat disimpan." }
            target
        } catch(e:Exception) { temp.delete();throw e }
        finally { preparing=false }
    }
}
