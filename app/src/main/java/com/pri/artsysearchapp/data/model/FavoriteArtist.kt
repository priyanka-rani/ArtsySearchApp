package com.pri.artsysearchapp.data.model

data class FavoriteArtist(
    val artistId: String,
    val name: String,
    val image: String,
    val birth_year: String? = null,
    val death_year: String? = null,
    val nationality: String? = null,
    val addedAt: String
)