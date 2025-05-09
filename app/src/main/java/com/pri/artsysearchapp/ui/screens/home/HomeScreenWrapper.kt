package com.pri.artsysearchapp.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreenWrapper(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshAuth()
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToLogin -> {
                    navController.navigate("login")
                }
                is HomeUiEvent.NavigateToSearch -> {
                    navController.navigate("search")
                }
                is HomeUiEvent.NavigateToArtistDetail -> {
                    navController.navigate("details/${event.artistId}")
                }
            }
        }
    }

    HomeScreen(
        isLoggedIn = isLoggedIn,
        favoriteArtists = favorites,
        viewModel = viewModel
    )
}