package com.pri.artsysearchapp.ui.screens.artistdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DetailsTab(uiState: ArtistDetailUiState) {
    val artist = uiState.artist ?: return
    val nationality = artist.nationality.orEmpty()
    val birthday = artist.birthday.orEmpty()
    val deathday = artist.deathday.orEmpty()

    val lifeInfo = buildString {
        if (nationality.isNotBlank()) append(nationality)
        if (birthday.isNotBlank() || deathday.isNotBlank()) {
            if (isNotEmpty()) append(", ")
            append("$birthday")
            if (birthday.isNotBlank() && deathday.isNotBlank()) append(" - ")
            if (birthday.isBlank() && deathday.isNotBlank()) append("- ")
            append(deathday)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            text = artist.name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if (lifeInfo.isNotBlank()) {
            Text(
                fontWeight = FontWeight.Bold,
                text = lifeInfo,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (!artist.biography.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = artist.biography, style = MaterialTheme.typography.bodyLarge)
        }
    }
}