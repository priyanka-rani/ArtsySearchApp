import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.Duration

fun getCurrentFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return LocalDate.now().format(formatter)
}

fun getRelativeTime(timestamp: Long): String {
    val duration = Duration.between(Instant.ofEpochMilli(timestamp), Instant.now())
    val minutesAgo = duration.toMinutes()
    return "$minutesAgo seconds ago" // Customize with Intl.RelativeTimeFormat or Formatter
}