package com.example.movil_1.data.repository

import com.example.movil_1.data.api.RetrofitClient
import com.example.movil_1.data.api.RickAndMortyApiService
import com.example.movil_1.data.model.CharacterDto
import com.example.movil_1.data.model.EpisodeDto

class RickAndMortyRepository(
    private val apiService: RickAndMortyApiService = RetrofitClient.apiService
) {
    suspend fun getCharacters(page: Int = 1, name: String? = null): Result<List<CharacterDto>> {
        return runCatching {
            val response = apiService.getCharacters(
                page = page,
                name = if (name.isNullOrBlank()) null else name.trim()
            )
            response.results
        }
    }

    suspend fun getEpisodes(page: Int = 1, name: String? = null): Result<List<EpisodeDto>> {
        return runCatching {
            val response = apiService.getEpisodes(
                page = page,
                name = if (name.isNullOrBlank()) null else name.trim()
            )
            response.results
        }
    }

    suspend fun getCharacterById(id: Int): Result<CharacterDto> {
        return runCatching {
            apiService.getCharacterById(id)
        }
    }
}
