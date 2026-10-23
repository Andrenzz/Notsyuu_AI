package id.notsyuu.assistant

import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class ReminderParserTest {
    private fun time(day:Int,hour:Int,minute:Int=0) = Calendar.getInstance().apply { clear(); set(2026,Calendar.OCTOBER,day,hour,minute) }.timeInMillis
    @Test fun relativeCrossesMidnight() { assertEquals(time(24,0,15),ReminderParser.parse("Ingatkan dalam 30 menit minum",time(23,23,45))) }
    @Test fun tomorrowAfternoon() { assertEquals(time(24,16),ReminderParser.parse("Ingatkan saya besok sore ambil paket",time(23,22))) }
    @Test fun eveningExplicitTime() { assertEquals(time(24,19,30),ReminderParser.parse("Besok jam 7.30 malam",time(23,22))) }
    @Test fun ambiguousOrInvalidNeedsPicker() { assertNull(ReminderParser.parse("ingatkan bayar listrik",time(23,10))); assertNull(ReminderParser.parse("jam 29.90",time(23,10))); assertNull(ReminderParser.parse("jam 8 pagi",time(23,10))); assertNull(ReminderParser.parse("dalam 99999999999999999999999 jam",time(23,10))) }
}
