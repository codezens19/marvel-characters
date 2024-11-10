package co.diwakar.marvelcharacters.presentation.characters_listings

sealed class MarvelCharactersListingEvent {
    object Refresh : MarvelCharactersListingEvent()
    object FetchNextPage : MarvelCharactersListingEvent()
    object ResetError : MarvelCharactersListingEvent()
    data class OnSearchQueryChange(val query: String) : MarvelCharactersListingEvent()
}