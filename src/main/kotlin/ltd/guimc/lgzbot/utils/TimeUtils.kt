package ltd.guimc.lgzbot.utils

import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object TimeUtils {
    fun convertDate(dateInMilliseconds: Long): String {
        return Instant.ofEpochMilli(dateInMilliseconds)
            .atZone(ZoneId.of("Asia/Shanghai"))
            .toLocalDateTime()
            .format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            )
    }
}