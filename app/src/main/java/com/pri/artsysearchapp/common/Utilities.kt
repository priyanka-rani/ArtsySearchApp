import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun getCurrentFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return LocalDate.now().format(formatter)
}

fun formatRelativeTime(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return ""

    return try {
        val dateTime = ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        val now = ZonedDateTime.now()
        val seconds = ChronoUnit.SECONDS.between(dateTime, now)
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            seconds < 60 -> "$seconds second${if (seconds != 1L) "s" else ""} ago"
            minutes < 60 -> "$minutes minute${if (minutes != 1L) "s" else ""} ago"
            hours < 24 -> "$hours hour${if (hours != 1L) "s" else ""} ago"
            else -> "$days day${if (days != 1L) "s" else ""} ago"
        }
    } catch (e: Exception) {
        ""
    }
}