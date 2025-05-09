package com.pri.artsysearchapp.ui.screens.artistdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pri.artsysearchapp.R
import com.pri.artsysearchapp.data.model.Category
import com.pri.artsysearchapp.ui.components.EmptyView

@Composable
fun ArtworksTab(
    uiState: ArtistDetailUiState,
    onViewCategories: (String) -> Unit,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    categories: List<Category>,
    isLoadingCategories: Boolean
) {
    if (uiState.artworks.isEmpty()) {
        EmptyView(text = "No Artworks")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.artworks) { artwork ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val imageUrl = artwork.links?.thumbnail?.href
                        val isValidImage = imageUrl != null && imageUrl != "/assets/shared/missing_image.png"

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = artwork.title,
                            placeholder = if (isValidImage) null else painterResource(id = R.drawable.artsy_logo),
                            error = painterResource(id = R.drawable.artsy_logo),
                            contentScale = if (isValidImage) ContentScale.Crop else ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${artwork.title}, ${artwork.date}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onViewCategories(artwork.id) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("View Categories")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        CategoryDialog(
            isLoading = isLoadingCategories,
            categories = categories,
            onClose = onDismissDialog
        )
    }
}