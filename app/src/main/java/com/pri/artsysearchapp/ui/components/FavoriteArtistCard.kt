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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pri.artsysearchapp.data.model.FavoriteArtist
import formatRelativeTime
import kotlinx.coroutines.delay

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