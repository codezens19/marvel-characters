package co.diwakar.marvelcharacters.presentation.characters_listings

import co.diwakar.marvelcharacters.domain.model.MarvelCharacter

data class MarvelCharactersListingState(
    val characters: List<MarvelCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String? = null,
    val offset: Int = 0,
    val isPaginationEnabled: Boolean = true,
    val errorMessage: String? = null,
) {
    fun progressCount(): Int {
        val isInitialCall = isLoading && characters.isEmpty()
        return if (isInitialCall) {
            //when DB is also empty
            6
        } else {
            //when fetching next page
            2
        }
    }
}