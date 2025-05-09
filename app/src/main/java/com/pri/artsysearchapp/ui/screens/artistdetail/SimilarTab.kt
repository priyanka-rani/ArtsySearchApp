package com.pri.artsysearchapp.ui.screens.artistdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pri.artsysearchapp.ui.components.ArtistCard
import com.pri.artsysearchapp.ui.components.EmptyView
import com.pri.artsysearchapp.ui.screens.home.HomeViewModel

@Composable
fun SimilarTab(uiState: ArtistDetailUiState, navController: NavController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val isLoggedIn by homeViewModel.isLoggedIn.collectAsState()
    val favorites by homeViewModel.favorites.collectAsState()
    if (uiState.similarArtists.isEmpty()) {
        EmptyView(text = "No Similar Artists")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.similarArtists) { artist ->
                ArtistCard(
                    artist = artist,
                    isLoggedIn = isLoggedIn,
                    isFavorite = favorites.any { it.artistId == artist.id },
                    onToggleFavorite = { homeViewModel.toggleFavorite(artist) },
                    onClick = { navController.navigate("details/${artist.id}") }
                )
            }
        }
    }
}