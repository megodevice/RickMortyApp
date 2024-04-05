package com.iliazusik.rickmortyapp.data.models

import com.google.gson.annotations.SerializedName

class Characters(

    @SerializedName("info")
    val requestInfo: Info,

    @SerializedName("results")
    val characters: List<Character>
)

data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String
)

data class Character(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String,
    var firsSeenEpisodeName: String?,
)

data class Location(
    val name: String,
    val url: String
)

data class Origin(
    val name: String,
    val url: String
)