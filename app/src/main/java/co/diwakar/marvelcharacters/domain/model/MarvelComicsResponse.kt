package co.diwakar.marvelcharacters.domain.model

data class MarvelComicsResponse(
    val data: MarvelComicsData?
)

data class MarvelComicsData(
    val offset: Int?,
    val total: Int?,
    val count: Int?,
    val results: List<MarvelComic>?
)