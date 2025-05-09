package com.pri.artsysearchapp.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: String,
    val name: String,
    @SerializedName("display_name") val displayName: String,
    val image: String,
    val description: String
)