package com.example.movil_1.data.api

import com.example.movil_1.data.model.CharacterDto
import com.example.movil_1.data.model.CharacterResponse
import com.example.movil_1.data.model.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDto

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null
    ): EpisodeResponse
}
