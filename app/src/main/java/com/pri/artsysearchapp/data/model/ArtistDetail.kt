package com.pri.artsysearchapp.data.model

import com.google.gson.annotations.SerializedName

data class ArtistDetail(
    val id: String,
    val name: String,
    val nationality: String? = null,
    val birthday: String? = null,
    val deathday: String? = null,
    val biography: String? = null,
    @SerializedName("_links") val links: Links
)

data class Artwork(
    val id: String,
    val title: String,
    @SerializedName("date") val date: String? = null,
    val links: Links
)

data class Links(
    val thumbnail: Link? = null
)

data class Link(
    val href: String
)