package co.diwakar.marvelcharacters.presentation.character_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.diwakar.marvelcharacters.domain.model.MarvelCharacter
import co.diwakar.marvelcharacters.domain.model.MarvelComicsData
import co.diwakar.marvelcharacters.domain.repository.MarvelCharactersRepository
import co.diwakar.marvelcharacters.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelCharacterDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MarvelCharactersRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MarvelCharacterDetailsState())
    val state = _state as StateFlow<MarvelCharacterDetailsState>

    init {
        viewModelScope.launch {
            val character =
                savedStateHandle.get<MarvelCharacter>("marvelCharacter") ?: return@launch

            val characterId = character.id
            if (character.isComicsPresent() && characterId != null) {
                fetchCharacterComics(characterId = characterId)
            }
        }
    }

    fun onEvent(event: MarvelCharacterDetailsEvent) {
        when (event) {
            is MarvelCharacterDetailsEvent.FetchNextPage -> {
                if (state.value.isPaginationEnabled && state.value.isLoading.not()) {
                    fetchCharacterComics(event.characterId)
                }
            }
            is MarvelCharacterDetailsEvent.ResetError -> onError(null)
        }
    }

    private fun fetchCharacterComics(characterId: Int) {
        viewModelScope.launch {
            repository.getMarvelCharacterComics(
                limit = LIMIT,
                offset = state.value.offset,
                characterId = characterId
            ).collect { result ->
                when (result) {
                    is Resource.Success -> onComicsRequestSuccess(result.data)
                    is Resource.Error -> onError(result.message)
                    is Resource.Loading -> onLoadUpdated(result.isLoading)
                }
            }
        }
    }

    private fun onComicsRequestSuccess(data: MarvelComicsData?) {
        data?.let { toSet ->
            _state.update {
                val newComics = listOf(it.comics, toSet.results ?: emptyList()).flatten()
                val newOffset = newComics.size
                val totalCount = toSet.total ?: 0

                it.copy(
                    comics = newComics,
                    offset = newOffset,
                    isPaginationEnabled = newOffset < totalCount
                )
            }
        }
    }

    private fun onError(message: String?) {
        _state.update {
            it.copy(errorMessage = message)
        }
    }

    private fun onLoadUpdated(isLoading: Boolean) {
        _state.update {
            it.copy(isLoading = isLoading)
        }
    }

    companion object {
        const val LIMIT = 10
    }
}