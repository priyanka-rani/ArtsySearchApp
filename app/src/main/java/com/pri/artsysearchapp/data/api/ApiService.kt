package com.pri.artsysearchapp.data.api

import com.pri.artsysearchapp.data.model.Artist
import com.pri.artsysearchapp.data.model.ArtistDetail
import com.pri.artsysearchapp.data.model.Artwork
import com.pri.artsysearchapp.data.model.Category
import com.pri.artsysearchapp.data.model.SearchResponse
import com.pri.artsysearchapp.data.model.FavoriteArtist
import com.pri.artsysearchapp.data.model.LoginRequest
import com.pri.artsysearchapp.data.model.RegisterRequest
import com.pri.artsysearchapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("me")
    suspend fun getCurrentUser(): Response<User>

    @GET("favorites")
    suspend fun getFavorites(): List<FavoriteArtist>

    @POST("favorites")
    suspend fun addFavorite(@Body artist: FavoriteArtist)

    @DELETE("favorites/{artistId}")
    suspend fun removeFavorite(@Path("artistId") artistId: String)

    @GET("search")
    suspend fun searchArtists(@Query("query") query: String): SearchResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<Unit>

    @POST("logout")
    suspend fun logout(): Response<Unit>

    @DELETE("account")
    suspend fun deleteAccount(): Response<Unit>

    @GET("artists/{id}")
    suspend fun getArtistDetail(@Path("id") artistId: String): ArtistDetail

    @GET("artists/{id}/artworks")
    suspend fun getArtistArtworks(@Path("id") artistId: String): List<Artwork>

    @GET("artists/{id}/similar")
    suspend fun getSimilarArtists(@Path("id") artistId: String): List<Artist>

    @GET("artworks/{id}/categories")
    suspend fun getCategories(@Path("id") artworkId: String): List<Category>

}