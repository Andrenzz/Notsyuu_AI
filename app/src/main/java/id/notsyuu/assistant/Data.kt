package id.notsyuu.assistant

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class Data(val context: Context) {
    companion object { private val lock = Any() }
    val prefs = context.getSharedPreferences("notsyuu", Context.MODE_PRIVATE)
    fun list(key: String): List<JSONObject> = synchronized(lock) {
        val a = JSONArray(prefs.getString(key, "[]"))
        (0 until a.length()).map { a.getJSONObject(it) }
    }
    fun save(key: String, items: List<JSONObject>) { synchronized(lock) { prefs.edit().putString(key, JSONArray(items).toString()).apply() } }
    fun add(key: String, obj: JSONObject): String = synchronized(lock) {
        val id = UUID.randomUUID().toString(); obj.put("id", id).put("created", System.currentTimeMillis())
        save(key, (list(key) + obj).takeLast(if (key == "history") 150 else 500)); id
    }
    fun log(text: String) { add("history", JSONObject().put("text", text)) }
    fun allowed(pkg: String) = canRead(pkg)
    fun canRead(pkg:String) = pkg != context.packageName && permissionSet("readAllowed").contains(pkg)
    fun canWrite(pkg:String) = pkg != context.packageName && permissionSet("writeAllowed").contains(pkg)
    fun permissionSet(key:String):Set<String> = prefs.getStringSet(key, if(key in listOf("readAllowed","writeAllowed")) prefs.getStringSet("allowed",emptySet()) else emptySet())!!.toSet()
    fun setPermission(scope:String,pkg:String,value:Boolean) {
        val key=when(scope) { "write"->"writeAllowed";"notice"->"noticeAllowed";else->"readAllowed" }
        val set=permissionSet(key).toMutableSet();if(value)set.add(pkg) else set.remove(pkg)
        prefs.edit().putStringSet(key,set).apply()
    }
    fun setAllPermissions(scope:String,packages:Set<String>,value:Boolean) {
        val key=when(scope) { "write"->"writeAllowed";else->"readAllowed" }
        prefs.edit().putStringSet(key,if(value)packages-context.packageName else emptySet()).apply()
    }
    fun profile() = prefs.getString("profile", "") ?: ""
    fun brief(): String {
        val notes = list("notes").filter { !it.optBoolean("done") }.joinToString("\n") { "• ${it.optString("text")}" }
        val now = System.currentTimeMillis()
        val reminders = list("alarms").filter { it.optLong("time") > now && it.optLong("time") < now + 86400000 }.joinToString("\n") { "• ${it.optString("text")}" }
        return "Tugas belum selesai:\n${notes.ifBlank { "Belum ada." }}\n\nPengingat 24 jam:\n${reminders.ifBlank { "Belum ada." }}"
    }
}
