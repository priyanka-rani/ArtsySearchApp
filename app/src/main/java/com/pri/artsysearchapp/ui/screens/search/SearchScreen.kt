package com.pri.artsysearchapp.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pri.artsysearchapp.data.model.Artist
import com.pri.artsysearchapp.ui.components.ArtistCard
import com.pri.artsysearchapp.ui.screens.home.HomeViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val isLoggedIn by homeViewModel.isLoggedIn.collectAsState()
    val favorites by homeViewModel.favorites.collectAsState()
    LaunchedEffect(Unit) {
        homeViewModel.refreshAuth()
    }
    SearchScreen(
        searchQuery = viewModel.searchQuery,
        onQueryChanged = viewModel::onQueryChanged,
        artists = viewModel.artists,
        isLoggedIn = isLoggedIn,
        favorites = favorites.map { it.artistId },
        onToggleFavorite = { homeViewModel.toggleFavorite(it) },
        onArtistClicked = { artist ->
            navController.navigate("details/${artist.id}")
        },
        navController = navController
    )
}

@Composable
fun SearchScreen(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    artists: List<Artist>,
    favorites: List<String>,
    isLoggedIn: Boolean,
    onToggleFavorite: (Artist) -> Unit,
    onArtistClicked: (Artist) -> Unit,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Search Bar
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                TextField(
                    value = searchQuery,
                    onValueChange = onQueryChanged,
                    placeholder = {
                        Text(
                            "Search artists...",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(onClick = {  if (searchQuery.isBlank()) {
                            navController.popBackStack()
                        } else {
                            onQueryChanged("")
                        } }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(artists) { artist ->
                val isFav = favorites.contains(artist.id)
                ArtistCard(
                    artist = artist,
                    isLoggedIn = isLoggedIn,
                    isFavorite = isFav,
                    onToggleFavorite = { onToggleFavorite(artist) },
                    onClick = { onArtistClicked(artist) }
                )
            }
        }
    }
}