package co.diwakar.marvelcharacters.presentation.character_details

import co.diwakar.marvelcharacters.domain.model.MarvelComic

data class MarvelCharacterDetailsState(
    val comics: List<MarvelComic> = emptyList(),
    val offset: Int = 0,
    val isPaginationEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
) {
    fun progressCount(): Int {
        val isInitialCall = isLoading && comics.isEmpty()
        return if (isInitialCall) {
            4
        } else {
            1
        }
    }
}