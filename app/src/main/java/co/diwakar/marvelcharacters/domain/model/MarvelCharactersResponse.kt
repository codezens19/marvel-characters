package co.diwakar.marvelcharacters.domain.model

data class MarvelCharactersResponse(
    val data: MarvelCharactersData?
)

data class MarvelCharactersData(
    val offset: Int?,
    val total: Int?,
    val count: Int?,
    val results: List<MarvelCharacter>?
)