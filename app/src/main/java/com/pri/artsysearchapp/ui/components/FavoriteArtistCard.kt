package com.pri.artsysearchapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pri.artsysearchapp.data.model.FavoriteArtist
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun FavoriteArtistCard(
    artist: FavoriteArtist,
    onClick: () -> Unit
) {
    var relativeTime by remember { mutableStateOf(formatRelativeTime(artist.addedAt)) }

    LaunchedEffect(Unit) {
        while (true) {
            relativeTime = formatRelativeTime(artist.addedAt)
            delay(1000L) // Update every second
        }
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = artist.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = listOfNotNull(artist.nationality, artist.birth_year).joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Text(
            text = relativeTime,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )

        // Arrow
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "View details"
        )
    }
}

private fun formatRelativeTime(timestamp: String?): String {
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