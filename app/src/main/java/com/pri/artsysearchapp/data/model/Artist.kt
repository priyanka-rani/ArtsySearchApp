package com.pri.artsysearchapp.data.model

import com.google.gson.annotations.SerializedName

data class Artist(
    val id: String,
    val name: String,
    @SerializedName("links", alternate = ["_links"])
    val links: Links
)