package id.notsyuu.assistant

import java.util.Calendar
import java.util.Locale

object ReminderParser {
    /** Returns null for ambiguous or past times so the UI asks the user. */
    fun parse(text: String, now: Long = System.currentTimeMillis()): Long? {
        val t=text.lowercase(Locale.ROOT)
        val relative=Regex("(?:dalam|lagi)\\s+(\\d+)\\s*(menit|jam)|(?:\\b)(\\d+)\\s*(menit|jam)\\s+lagi").find(t)
        if(relative!=null) {
            val n=(relative.groupValues[1].ifBlank { relative.groupValues[3] }).toLongOrNull() ?: return null
            val unit=relative.groupValues[2].ifBlank { relative.groupValues[4] }
            if(n !in 1..10080) return null
            val delta=n*(if(unit=="jam") 3600000 else 60000)
            if(delta>7*86400000L) return null
            return now+delta
        }
        val day=when { t.contains("lusa") -> 2; t.contains("besok") -> 1; else -> 0 }
        val exact=Regex("(?:jam|pukul)\\s+(\\d{1,2})(?:[.:](\\d{2}))?").find(t)
        var hour=exact?.groupValues?.get(1)?.toIntOrNull()
        val minute=exact?.groupValues?.get(2)?.toIntOrNull() ?: 0
        if(hour!=null) { if(hour !in 0..23 || minute !in 0..59) return null; if(hour in 1..11 && (t.contains("malam")||t.contains("sore")||t.contains("siang"))) hour+=12; if(hour==12 && t.contains("pagi")) hour=0 }
        else hour=when { t.contains("pagi") -> 8; t.contains("siang") -> 12; t.contains("sore") -> 16; t.contains("malam") -> 20; else -> return null }
        val c=Calendar.getInstance().apply { timeInMillis=now; add(Calendar.DAY_OF_YEAR,day); set(Calendar.HOUR_OF_DAY,hour!!); set(Calendar.MINUTE,minute); set(Calendar.SECOND,0); set(Calendar.MILLISECOND,0) }
        return c.timeInMillis.takeIf { it>now }
    }
}
