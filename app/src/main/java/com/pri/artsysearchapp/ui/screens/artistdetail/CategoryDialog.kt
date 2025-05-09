package com.pri.artsysearchapp.ui.screens.artistdetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pri.artsysearchapp.data.model.Category
import com.pri.artsysearchapp.ui.components.LoadingText

@Composable
fun CategoryDialog(
    isLoading: Boolean,
    categories: List<Category>,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(onClick = onClose) {
                Text("Close")
            }
        },
        title = {
            Text("Categories", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            if (isLoading) {
                LoadingText()
            } else {
                if (categories.isEmpty()) {
                    Text("No Categories available")
                } else {
                    CategoryCarousel(categories = categories)
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CategoryCarousel(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val parentWidth = maxWidth
        val cardWidth = parentWidth * 0.9f // 80% of the screen
        val spacing = 8.dp
        val selectedIndex = remember { mutableStateOf(0) }

        LaunchedEffect(selectedIndex.value) {
            listState.animateScrollToItem(index = selectedIndex.value)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(categories) { index, category ->
                    Card(
                        modifier = Modifier.width(cardWidth),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth())
                        {
                            AsyncImage(
                                model = category.image,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(8.dp))
                                val scrollState = rememberScrollState()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .heightIn(min = 60.dp, max = 120.dp)
                                        .verticalScroll(scrollState)
                                ) {
                                    Text(
                                        text = category.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        selectedIndex.value =
                            (selectedIndex.value - 1 + categories.size) % categories.size
                    },
                    modifier = Modifier.offset(x = (-16).dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous"
                    )
                }

                IconButton(
                    onClick = {
                        selectedIndex.value = (selectedIndex.value + 1) % categories.size
                    },
                    modifier = Modifier.offset(x = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}