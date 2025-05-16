package com.pri.artsysearchapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pri.artsysearchapp.common.SnackBarManager
import com.pri.artsysearchapp.data.model.Artist
import com.pri.artsysearchapp.data.model.ArtistDetail
import com.pri.artsysearchapp.data.model.FavoriteArtist
import com.pri.artsysearchapp.data.model.User
import com.pri.artsysearchapp.data.repository.ArtsyRepository
import com.pri.artsysearchapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

sealed class HomeUiEvent {
    object NavigateToLogin : HomeUiEvent()
    object NavigateToSearch : HomeUiEvent()
    data class NavigateToArtistDetail(val artistId: String) : HomeUiEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ArtsyRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _favorites = MutableStateFlow<List<FavoriteArtist>>(emptyList())
    val favorites: StateFlow<List<FavoriteArtist>> = _favorites.asStateFlow()

    private val _eventFlow = Channel<HomeUiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            checkAuth()
        }
    }

    private suspend fun checkAuth() {
        _user.value = repository.getCurrentUser()
        _isLoggedIn.value = _user.value != null
        if (_isLoggedIn.value) {
            loadFavorites()
        }
    }

    private suspend fun loadFavorites() {
        _favorites.value = repository.getFavoriteArtists()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            clearSession()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
            clearSession()
        }
    }

    private fun clearSession() {
        _user.value = null
        _isLoggedIn.value = false
        _favorites.value = emptyList()
        authRepository.clearSessionCookies()
    }

    fun navigateToLogin() {
        viewModelScope.launch {
            _eventFlow.send(HomeUiEvent.NavigateToLogin)
        }
    }

    fun navigateToArtistDetails(artist: FavoriteArtist) {
        viewModelScope.launch {
            _eventFlow.send(HomeUiEvent.NavigateToArtistDetail(artist.artistId))
        }
    }

    fun navigateToSearch() {
        viewModelScope.launch {
            _eventFlow.send(HomeUiEvent.NavigateToSearch)
        }
    }

    fun refreshAuth() {
        viewModelScope.launch {
            checkAuth()
        }
    }

    fun toggleFavorite(artist:Artist) {
        if (!_isLoggedIn.value) return

        viewModelScope.launch {
            val favoriteArtists = _favorites.value
            if (favoriteArtists.any { it.artistId == artist.id }) {
                removeFavorites(artist.id)
            } else {
                val artistDetail = repository.getArtistDetail(artist.id)
                addFavorites(artistDetail)
            }
        }
    }

    fun toggleFavorite(artist: ArtistDetail?) {
        if (!_isLoggedIn.value || artist == null) return

        viewModelScope.launch {
            val favoriteArtists = _favorites.value
            if (favoriteArtists.any { it.artistId == artist.id }) {
               removeFavorites(artist.id)
            } else { addFavorites(artist)
            }
        }
    }

    private suspend fun removeFavorites(artistId: String) {
        _favorites.value = _favorites.value.filterNot { it.artistId == artistId }
        repository.removeFavorite(artistId)
        SnackBarManager.showMessage("Removed from Favorites")
    }

    private suspend fun addFavorites(artist: ArtistDetail?) {
        if(artist == null) return
        val favoriteArtist = FavoriteArtist(
            artistId = artist.id,
            name = artist.name,
            image = artist.links.thumbnail?.href ?: "",
            birth_year = artist.birthday,
            death_year = artist.deathday,
            nationality = artist.nationality,
            addedAt = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        )
        _favorites.value = _favorites.value + favoriteArtist
        repository.addFavorite(favoriteArtist)
        SnackBarManager.showMessage("Added to Favorites")
    }
}