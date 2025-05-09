package com.pri.artsysearchapp.ui.screens.artistdetail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pri.artsysearchapp.data.model.Artist
import com.pri.artsysearchapp.data.model.ArtistDetail
import com.pri.artsysearchapp.data.model.Artwork
import com.pri.artsysearchapp.data.model.Category
import com.pri.artsysearchapp.data.repository.ArtsyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val repository: ArtsyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArtistDetailUiState())
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    val showDialog = mutableStateOf(false)
    val selectedCategories = mutableStateListOf<Category>()
    val isLoadingCategories = mutableStateOf(false)

    fun loadArtistData(artistId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val artist = repository.getArtistDetail(artistId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                artist = artist
            )
        }
    }

    fun loadArtworks(artistId: String) {
        if (_uiState.value.artworks.isNotEmpty() || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val artworks = repository.getArtworksForArtist(artistId)
            _uiState.value = _uiState.value.copy(
                artworks = artworks,
                isLoading = false
            )
        }
    }

    fun loadSimilarArtists(artistId: String) {
        if (_uiState.value.similarArtists.isNotEmpty() || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val similar = repository.getSimilarArtists(artistId)
            _uiState.value = _uiState.value.copy(
                similarArtists = similar,
                isLoading = false
            )
        }
    }

    fun fetchCategories(artworkId: String) {
        viewModelScope.launch {
            showDialog.value = true
            isLoadingCategories.value = true
            val result = repository.getCategories(artworkId)
            selectedCategories.clear()
            selectedCategories.addAll(result)
            isLoadingCategories.value = false
        }
    }
}

data class ArtistDetailUiState(
    val isLoading: Boolean = false,
    val artist: ArtistDetail? = null,
    val artworks: List<Artwork> = emptyList(),
    val similarArtists: List<Artist> = emptyList()
)