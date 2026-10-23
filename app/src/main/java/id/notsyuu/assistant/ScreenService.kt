package id.notsyuu.assistant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.view.accessibility.*
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.json.JSONObject

class ScreenService : AccessibilityService() {
    companion object { @Volatile var current: ScreenService? = null }
    override fun onServiceConnected() { current = this }
    override fun onDestroy() { current = null; super.onDestroy() }
    override fun onInterrupt() {}
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    private fun root(): AccessibilityNodeInfo? = windows.mapNotNull { it.root }.firstOrNull { Data(this).allowed(it.packageName?.toString() ?: "") }
    fun snapshot(): String {
        val r = root() ?: return "Tidak ada layar aplikasi yang diizinkan. Tambahkan aplikasi pada menu Akses, lalu tampilkan aplikasinya."
        val result = StringBuilder("Aplikasi: ${r.packageName}\n")
        fun visit(n: AccessibilityNodeInfo, depth: Int) {
            if (depth > 25 || result.length > 14000) return
            if (n.isPassword) return
            val text = n.text?.toString()?.ifBlank { null } ?: n.contentDescription?.toString()
            if (!text.isNullOrBlank()) result.append(text.take(500)).append(if (n.isClickable) " [tombol]" else "").append('\n')
            for (i in 0 until n.childCount) n.getChild(i)?.let { visit(it, depth + 1) }
        }
        visit(r, 0); return result.toString()
    }
    fun act(kind: String, value: String, expectedPackage: String): Boolean {
        val r = root() ?: return false
        if (r.packageName?.toString() != expectedPackage) return false
        fun find(n: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            if (n.isPassword) return null
            if (kind == "type" && n.isEditable && (n.isFocused || n.isAccessibilityFocused)) return n
            if (kind == "tap" && (n.text?.toString()?.equals(value, true) == true || n.contentDescription?.toString()?.equals(value, true) == true)) return n
            if (kind == "scroll" && n.isScrollable) return n
            for (i in 0 until n.childCount) n.getChild(i)?.let { find(it)?.let { found -> return found } }
            return null
        }
        val n = find(r) ?: return false
        return when (kind) {
            "tap" -> { var target = n; while (!target.isClickable && target.parent != null) target = target.parent; target.performAction(AccessibilityNodeInfo.ACTION_CLICK) }
            "type" -> n.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply { putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, value) })
            "scroll" -> n.performAction(if (value == "up") AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD else AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
            else -> false
        }
    }
}
class NoticeService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val d = Data(this)
        if (!d.prefs.getStringSet("noticeAllowed", emptySet())!!.contains(sbn.packageName)) return
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        // Authentication codes are excluded from storage and summaries.
        if (Regex("(?i)otp|verification|verifikasi|kode masuk|password|sandi|\\b\\d{4,8}\\b").containsMatchIn("$title $text")) return
        val obj = JSONObject().put("package", sbn.packageName).put("key", sbn.key).put("text", "$title: $text".take(1200)).put("time", System.currentTimeMillis())
        d.save("notices", (d.list("notices").filter { it.optString("key") != sbn.key && it.optLong("time") > System.currentTimeMillis()-86400000 } + obj).takeLast(60))
    }
}
