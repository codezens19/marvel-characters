package co.diwakar.marvelcharacters.domain.repository

import co.diwakar.marvelcharacters.domain.model.MarvelCharacter
import co.diwakar.marvelcharacters.domain.model.MarvelCharactersData
import co.diwakar.marvelcharacters.domain.model.MarvelComicsData
import co.diwakar.marvelcharacters.util.Resource
import kotlinx.coroutines.flow.Flow

interface MarvelCharactersRepository {
    suspend fun getMarvelCharactersFromCache(): Flow<Resource<List<MarvelCharacter>>>

    suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int,
        query: String? = null
    ): Flow<Resource<MarvelCharactersData>>

    suspend fun getMarvelCharacterComics(
        characterId: Int,
        limit: Int,
        offset: Int
    ): Flow<Resource<MarvelComicsData>>
}