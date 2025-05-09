package com.pri.artsysearchapp.data.repository

import android.util.Log
import com.pri.artsysearchapp.data.api.ApiService
import com.pri.artsysearchapp.data.model.Artist
import com.pri.artsysearchapp.data.model.ArtistDetail
import com.pri.artsysearchapp.data.model.Artwork
import com.pri.artsysearchapp.data.model.Category
import com.pri.artsysearchapp.data.model.FavoriteArtist
import com.pri.artsysearchapp.data.model.User

class ArtsyRepository(private val api: ApiService) {

    suspend fun searchArtists(query: String): List<Artist> {
        return try {
            api.searchArtists(query).artists
        } catch (e: Exception) {
            Log.e("SearchRepo", "Search failed", e)
            emptyList()
        }
    }

    suspend fun getCurrentUser(): User? {
        return try {
            api.getCurrentUser().body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFavoriteArtists(): List<FavoriteArtist> {
        return try {
            api.getFavorites()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addFavorite(artist: FavoriteArtist): Boolean {
        return try {
            api.addFavorite(artist)
            true
        } catch (e: Exception) {
            Log.e("Repo", "Add favorite failed", e)
            false
        }
    }

    suspend fun removeFavorite(artistId: String): Boolean {
        return try {
            api.removeFavorite(artistId)
            true
        } catch (e: Exception) {
            Log.e("Repo", "Remove favorite failed", e)
            false
        }
    }


    suspend fun getArtistDetail(id: String): ArtistDetail? {
        return try {
            api.getArtistDetail(id)
        } catch (e: Exception) {
            Log.e("Repo", "getArtistDetail failed", e)
            null
        }
    }

    suspend fun getArtworksForArtist(id: String): List<Artwork> {
        return try {
            api.getArtistArtworks(id)
        } catch (e: Exception) {
            Log.e("Repo", "getArtworks failed", e)
            emptyList()
        }
    }

    suspend fun getSimilarArtists(id: String): List<Artist> {
        return try {
            api.getSimilarArtists(id)
        } catch (e: Exception) {
            Log.e("Repo", "getSimilarArtists failed", e)
            emptyList()
        }
    }
    suspend fun getCategories(artworkId: String): List<Category> {
        return try {
            api.getCategories(artworkId)
        } catch (e: Exception) {
            Log.e("Repo", "Failed to fetch categories for $artworkId", e)
            emptyList()
        }
    }
}