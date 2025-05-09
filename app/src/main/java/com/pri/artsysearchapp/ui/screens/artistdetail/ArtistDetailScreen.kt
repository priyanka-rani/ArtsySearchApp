package com.pri.artsysearchapp.ui.screens.artistdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pri.artsysearchapp.ui.components.AppBar
import com.pri.artsysearchapp.ui.components.LoadingText
import com.pri.artsysearchapp.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistId: String,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by homeViewModel.isLoggedIn.collectAsState()
    val selectedTab = rememberSaveable { mutableStateOf(0) }
    val favorites by homeViewModel.favorites.collectAsState()
    LaunchedEffect(Unit) {
        homeViewModel.refreshAuth()
    }
    LaunchedEffect(artistId) {
        viewModel.loadArtistData(artistId)
    }

    Scaffold(
        topBar = {
            AppBar(
                title = uiState.artist?.name ?: "Artist Details",
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                actions = {
                    if (isLoggedIn && uiState.artist != null) {
                        IconButton(onClick = { homeViewModel.toggleFavorite(uiState.artist) }) {
                            val isFavorite = favorites.any { it.artistId == artistId }
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorite"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val tabTitles = listOf(
                "Details",
                "Artworks"
            ) + if (isLoggedIn) listOf("Similar") else emptyList()

            TabRow(selectedTabIndex = selectedTab.value) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab.value == index,
                        onClick = {
                            selectedTab.value = index
                            when (title) {
                                "Artworks" -> viewModel.loadArtworks(artistId)
                                "Similar" -> viewModel.loadSimilarArtists(artistId)
                            }
                        },
                        text = { Text(title) },
                        icon = {
                            Icon(
                                imageVector = when (title) {
                                    "Details" -> Icons.Outlined.Info
                                    "Artworks" -> Icons.Outlined.AccountBox
                                    else -> Icons.Outlined.PersonSearch
                                },
                                contentDescription = title
                            )
                        }
                    )
                }
            }
            if (uiState.isLoading) {
                LoadingText()
            } else {

                when (selectedTab.value) {
                    0 -> DetailsTab(uiState)
                    1 -> ArtworksTab(
                        uiState = uiState,
                        onViewCategories = { artistId -> viewModel.fetchCategories(artistId) },
                        showDialog = viewModel.showDialog.value,
                        onDismissDialog = { viewModel.showDialog.value = false },
                        categories = viewModel.selectedCategories,
                        isLoadingCategories = viewModel.isLoadingCategories.value
                    )

                    2 -> if (isLoggedIn) SimilarTab(uiState, navController)
                }
            }
        }
    }
}


