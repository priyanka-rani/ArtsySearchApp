package com.pri.artsysearchapp.ui.screens.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pri.artsysearchapp.data.model.FavoriteArtist
import com.pri.artsysearchapp.ui.components.AppBar
import com.pri.artsysearchapp.ui.components.FavoriteArtistCard
import com.pri.artsysearchapp.ui.components.EmptyView
import getCurrentFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isLoggedIn: Boolean,
    favoriteArtists: List<FavoriteArtist>,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.user.collectAsState()
    var isUserFetched by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        if (user != null) {
            isUserFetched = true
        }
    }

    Column(modifier.fillMaxSize()) {
        AppBar(
            title = "Artist Search",
            actions = {
                IconButton(onClick = viewModel::navigateToSearch) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                if (isLoggedIn) {
                    val user by viewModel.user.collectAsState()
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user?.avatar)
                                .crossfade(true)
                                .build(),
                            contentDescription = "User Profile",
                            modifier = Modifier
                                .height(32.dp)
                                .padding(4.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Log out") },
                            onClick = {
                                expanded = false
                                viewModel.logout()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete account", color = Color.Red) },
                            onClick = {
                                expanded = false
                                viewModel.deleteAccount()
                            }
                        )
                    }
                } else {
                    IconButton(onClick = viewModel::navigateToLogin) {
                        Icon(imageVector = Icons.Outlined.PersonOutline, contentDescription = "User")
                    }

                }
            }

        )

        Text(
            text = getCurrentFormattedDate(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth()){
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(4.dp).align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        when {
            !isLoggedIn -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(onClick = viewModel::navigateToLogin) {
                        Text("Log in to see favorites")
                    }
                }
            }

            isLoggedIn && favoriteArtists.isEmpty() -> {
                EmptyView(text = "No favorites")
            }

            isLoggedIn -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favoriteArtists.sortedByDescending { it.addedAt }) { artist ->
                        FavoriteArtistCard(
                            artist = artist,
                            onClick = { viewModel.navigateToArtistDetails(artist) })
                    }
                }
            }
        }
        PoweredByArtsyLink()
    }
}

@Composable
fun PoweredByArtsyLink() {
    val context = LocalContext.current

    Text(
        text = "Powered by Artsy",
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, "https://www.artsy.net/".toUri())
                context.startActivity(intent)
            }
            .padding(16.dp)
    )
}