package com.example.movil_1.data.model

import com.google.gson.annotations.SerializedName

data class PageInfo(
    @SerializedName("count") val count: Int = 0,
    @SerializedName("pages") val pages: Int = 0,
    @SerializedName("next") val next: String? = null,
    @SerializedName("prev") val prev: String? = null
)

data class CharacterResponse(
    @SerializedName("info") val info: PageInfo = PageInfo(),
    @SerializedName("results") val results: List<CharacterDto> = emptyList()
)

data class CharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("origin") val origin: LocationRefDto? = null,
    @SerializedName("location") val location: LocationRefDto? = null,
    @SerializedName("image") val image: String = "",
    @SerializedName("episode") val episode: List<String> = emptyList(),
    @SerializedName("url") val url: String = "",
    @SerializedName("created") val created: String = ""
)

data class LocationRefDto(
    @SerializedName("name") val name: String = "",
    @SerializedName("url") val url: String = ""
)

data class EpisodeResponse(
    @SerializedName("info") val info: PageInfo = PageInfo(),
    @SerializedName("results") val results: List<EpisodeDto> = emptyList()
)

data class EpisodeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("air_date") val airDate: String = "",
    @SerializedName("episode") val episode: String = "",
    @SerializedName("characters") val characters: List<String> = emptyList(),
    @SerializedName("url") val url: String = "",
    @SerializedName("created") val created: String = ""
)
