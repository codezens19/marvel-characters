package co.diwakar.marvelcharacters.presentation.characters_listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.diwakar.marvelcharacters.domain.model.MarvelCharactersData
import co.diwakar.marvelcharacters.domain.repository.MarvelCharactersRepository
import co.diwakar.marvelcharacters.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelCharactersListingViewModel @Inject constructor(
    private val repository: MarvelCharactersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MarvelCharactersListingState())
    val state = _state as StateFlow<MarvelCharactersListingState>
    private var searchJob: Job? = null

    init {
        getMarvelCharactersListingsFromCache()
        getMarvelCharactersListings()
    }

    fun onEvent(event: MarvelCharactersListingEvent) {
        when (event) {
            is MarvelCharactersListingEvent.Refresh -> {
                _state.update {
                    it.copy(offset = 0)
                }
                getMarvelCharactersListings()
            }
            is MarvelCharactersListingEvent.FetchNextPage -> {
                if (state.value.isPaginationEnabled && state.value.isLoading.not()) {
                    getMarvelCharactersListings()
                }
            }
            is MarvelCharactersListingEvent.OnSearchQueryChange -> onSearchQuery(event.query)
            is MarvelCharactersListingEvent.ResetError -> onError(message = null)
        }
    }

    private fun onSearchQuery(toSearch: String) {
        _state.update {
            val query = toSearch.ifBlank { null }
            it.copy(searchQuery = query, offset = 0)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getMarvelCharactersListings()
        }
    }

    private fun getMarvelCharactersListingsFromCache() {
        viewModelScope.launch {
            repository.getMarvelCharactersFromCache().collect { result ->
                result.data?.let { characters ->
                    _state.update {
                        it.copy(characters = characters)
                    }
                }
            }
        }
    }

    private fun getMarvelCharactersListings() {
        viewModelScope.launch {
            repository.getMarvelCharacters(
                limit = LIMIT,
                offset = state.value.offset,
                query = state.value.searchQuery
            ).collect { result ->
                when (result) {
                    is Resource.Success -> onRemoteRequestSuccess(result.data)
                    is Resource.Error -> onError(result.message)
                    is Resource.Loading -> onLoadUpdated(result.isLoading)
                }
            }
        }
    }

    private fun onRemoteRequestSuccess(data: MarvelCharactersData?) {
        data?.let { toSet ->
            _state.update {
                val prevCharacters = if (it.offset == 0) {
                    //if previous characters are fetched from local
                    emptyList()
                } else {
                    //then we will not consider them
                    it.characters
                }
                val newCharacters = listOf(prevCharacters, toSet.results ?: emptyList()).flatten()
                val newOffset = newCharacters.size
                val totalCount = toSet.total ?: 0

                it.copy(
                    characters = newCharacters,
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