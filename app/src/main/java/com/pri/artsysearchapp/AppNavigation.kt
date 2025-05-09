package com.pri.artsysearchapp

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pri.artsysearchapp.common.SnackbarManager
import com.pri.artsysearchapp.ui.screens.artistdetail.ArtistDetailScreen
import com.pri.artsysearchapp.ui.screens.home.HomeScreenWrapper
import com.pri.artsysearchapp.ui.screens.login.LoginScreen
import com.pri.artsysearchapp.ui.screens.register.RegisterScreen
import com.pri.artsysearchapp.ui.screens.search.SearchScreen
import com.pri.artsysearchapp.ui.screens.search.SearchViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    // Initialize the global manager
    LaunchedEffect(Unit) {
        SnackbarManager.init(snackbarHostState, scope)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        NavHost(navController, startDestination = "home") {
            composable("home") {
                HomeScreenWrapper(navController = navController)
            }
            composable("search") {
                val searchViewModel: SearchViewModel = hiltViewModel()
                SearchScreen(viewModel = searchViewModel, navController = navController)
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    navController = navController
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    navController = navController
                )
            }
            composable("details/{artistId}") { navBackStackEntry ->
                val artistId = navBackStackEntry.arguments?.getString("artistId") ?: return@composable
                ArtistDetailScreen(
                    artistId = artistId,
                    navController = navController
                )
            }
        }
    }
}