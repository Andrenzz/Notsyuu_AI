package id.notsyuu.assistant

import android.app.*
import android.content.*
import android.provider.Settings
import org.json.JSONObject
import java.util.Calendar

object Scheduler {
    fun channel(c: Context) { c.getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel("reminders", "Pengingat Notsyuu", NotificationManager.IMPORTANCE_HIGH)) }
    fun schedule(c: Context, item: JSONObject) {
        val pi = PendingIntent.getBroadcast(c, item.getString("id").hashCode(), Intent(c, AlarmReceiver::class.java).putExtra("id", item.getString("id")), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        // Inexact alarms avoid requiring restricted exact-alarm permission.
        c.getSystemService(AlarmManager::class.java).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, item.getLong("time"), pi)
    }
    fun add(c: Context, text: String, time: Long, daily: Boolean = false, focus: Boolean = false) {
        val d = Data(c); d.add("alarms", JSONObject().put("text", text).put("time", time).put("daily", daily).put("focus", focus)); schedule(c, d.list("alarms").last())
    }
    fun cancel(c: Context, item: JSONObject) {
        val pi = PendingIntent.getBroadcast(c, item.getString("id").hashCode(), Intent(c, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        if (pi != null) { c.getSystemService(AlarmManager::class.java).cancel(pi); pi.cancel() }
        val d = Data(c); d.save("alarms", d.list("alarms").filter { it.optString("id") != item.optString("id") })
        if(item.optBoolean("focus")) {
            val nm=c.getSystemService(NotificationManager::class.java)
            if(nm.isNotificationPolicyAccessGranted) nm.setInterruptionFilter(d.prefs.getInt("previousDnd",NotificationManager.INTERRUPTION_FILTER_ALL))
        }
    }
    fun next(hour: Int, minute: Int): Long = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, hour); set(Calendar.MINUTE, minute); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0); if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1) }.timeInMillis
}
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, intent: Intent) {
        val d = Data(c); val item = d.list("alarms").find { it.optString("id") == intent.getStringExtra("id") } ?: return
        Scheduler.channel(c)
        if (item.optBoolean("focus")) {
            val nm = c.getSystemService(NotificationManager::class.java)
            if (nm.isNotificationPolicyAccessGranted) nm.setInterruptionFilter(d.prefs.getInt("previousDnd", NotificationManager.INTERRUPTION_FILTER_ALL))
        }
        val text = if (item.optBoolean("daily")) d.brief() else item.optString("text")
        val open = PendingIntent.getActivity(c, 0, Intent(c, MainActivity::class.java).putExtra("brief", text), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        try { c.getSystemService(NotificationManager::class.java).notify(item.optString("id").hashCode(), Notification.Builder(c, "reminders").setSmallIcon(id.notsyuu.assistant.R.drawable.ic_notsyuu).setContentTitle(item.optString("text")).setContentText(text).setStyle(Notification.BigTextStyle().bigText(text)).setContentIntent(open).setAutoCancel(true).build()) } catch (_: SecurityException) { d.log("Notifikasi pengingat diblokir; aktifkan izin notifikasi.") }
        d.log("Pengingat jatuh tempo: ${item.optString("text")}")
        if (item.optBoolean("daily")) {
            val next = Calendar.getInstance().apply { timeInMillis = item.optLong("time"); do { add(Calendar.DAY_OF_YEAR, 1) } while (timeInMillis <= System.currentTimeMillis()) }.timeInMillis
            item.put("time", next); d.save("alarms", d.list("alarms").map { if (it.optString("id") == item.optString("id")) item else it }); Scheduler.schedule(c, item)
        } else d.save("alarms", d.list("alarms").filter { it.optString("id") != item.optString("id") })
    }
}
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        Data(c).list("alarms").forEach { if (it.optLong("time") < System.currentTimeMillis()) it.put("time", System.currentTimeMillis() + 60000); Scheduler.schedule(c, it) }
    }
}
