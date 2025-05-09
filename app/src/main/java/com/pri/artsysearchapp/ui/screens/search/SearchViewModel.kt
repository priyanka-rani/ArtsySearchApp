package com.pri.artsysearchapp.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pri.artsysearchapp.data.model.Artist
import com.pri.artsysearchapp.data.repository.ArtsyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ArtsyRepository
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var artists = mutableStateListOf<Artist>()
        private set

    fun onQueryChanged(query: String) {
        searchQuery = query
        if (query.length >= 3) {
            viewModelScope.launch {
                val results = repository.searchArtists(query)
                artists.clear()
                artists.addAll(results)
            }
        } else {
            artists.clear()
        }
    }
}