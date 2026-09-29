package com.example.movil_1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_1.data.model.CharacterDto
import com.example.movil_1.data.model.EpisodeDto
import com.example.movil_1.data.repository.RickAndMortyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExploreUiState(
    val characters: List<CharacterDto> = emptyList(),
    val episodes: List<EpisodeDto> = emptyList(),
    val isLoadingCharacters: Boolean = false,
    val isLoadingEpisodes: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val selectedCharacter: CharacterDto? = null
)

class ExploreViewModel(
    private val repository: RickAndMortyRepository = RickAndMortyRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCharacters()
        loadEpisodes()
    }

    fun loadCharacters(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingCharacters = true, errorMessage = null)
            val result = repository.getCharacters(page = 1, name = query)
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    characters = list,
                    isLoadingCharacters = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    characters = emptyList(),
                    isLoadingCharacters = false,
                    errorMessage = error.localizedMessage ?: "Error al cargar personajes"
                )
            }
        }
    }

    fun loadEpisodes(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingEpisodes = true)
            val result = repository.getEpisodes(page = 1, name = query)
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    episodes = list,
                    isLoadingEpisodes = false
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoadingEpisodes = false
                )
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)

        // Cancel previous search and debounce by 400ms
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            loadCharacters(newQuery.ifBlank { null })
            loadEpisodes(newQuery.ifBlank { null })
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectCharacter(character: CharacterDto?) {
        _uiState.value = _uiState.value.copy(selectedCharacter = character)
    }
}
