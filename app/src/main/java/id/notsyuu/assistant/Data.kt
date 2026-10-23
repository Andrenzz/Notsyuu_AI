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
    fun allowed(pkg: String) = pkg != context.packageName && prefs.getStringSet("allowed", emptySet())!!.contains(pkg)
    fun profile() = prefs.getString("profile", "") ?: ""
    fun brief(): String {
        val notes = list("notes").filter { !it.optBoolean("done") }.joinToString("\n") { "• ${it.optString("text")}" }
        val now = System.currentTimeMillis()
        val reminders = list("alarms").filter { it.optLong("time") > now && it.optLong("time") < now + 86400000 }.joinToString("\n") { "• ${it.optString("text")}" }
        return "Tugas belum selesai:\n${notes.ifBlank { "Belum ada." }}\n\nPengingat 24 jam:\n${reminders.ifBlank { "Belum ada." }}"
    }
}
