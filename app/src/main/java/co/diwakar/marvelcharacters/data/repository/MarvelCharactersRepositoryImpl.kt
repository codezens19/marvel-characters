package co.diwakar.marvelcharacters.data.repository

import co.diwakar.marvelcharacters.data.local.MarvelCharacterDatabase
import co.diwakar.marvelcharacters.data.mapper.toMarvelCharactersListing
import co.diwakar.marvelcharacters.data.mapper.toMarvelCharactersListingEntity
import co.diwakar.marvelcharacters.data.remote.MarvelApi
import co.diwakar.marvelcharacters.domain.model.MarvelCharacter
import co.diwakar.marvelcharacters.domain.model.MarvelCharactersData
import co.diwakar.marvelcharacters.domain.model.MarvelComicsData
import co.diwakar.marvelcharacters.domain.repository.MarvelCharactersRepository
import co.diwakar.marvelcharacters.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarvelCharactersRepositoryImpl @Inject constructor(
    private val api: MarvelApi,
    db: MarvelCharacterDatabase
) : MarvelCharactersRepository {
    private val dao = db.dao

    override suspend fun getMarvelCharactersFromCache(): Flow<Resource<List<MarvelCharacter>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.getMarvelCharactersListing()
            emit(
                Resource.Success(
                    data = localListings.map { it.toMarvelCharactersListing() }
                )
            )
        }
    }

    override suspend fun getMarvelCharacters(
        limit: Int,
        offset: Int,
        query: String?
    ): Flow<Resource<MarvelCharactersData>> {
        return flow {
            emit(Resource.Loading(true))
            val remoteData = try {
                val response = api.getCharacters(limit = limit, offset = offset, query = query)
                response.data
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
                null
            }

            remoteData?.let { data ->
                val remoteListings = data.results
                remoteListings?.let { listings ->
                    /**
                     * store data in local storage if it is the first API call
                     * for characters listing
                     * */
                    val shouldSaveInCache = offset == 0 && query.isNullOrBlank()
                    if (shouldSaveInCache) {
                        dao.clearMarvelCharactersListing()
                        dao.insertMarvelCharactersListings(
                            listings.map { it.toMarvelCharactersListingEntity() }
                        )
                    }
                }

                emit(Resource.Success(data = data))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getMarvelCharacterComics(
        characterId: Int,
        limit: Int,
        offset: Int
    ): Flow<Resource<MarvelComicsData>> {
        return flow {
            emit(Resource.Loading(true))
            val remoteData = try {
                val response = api.getCharacterComics(
                    limit = limit,
                    offset = offset,
                    characterId = characterId
                )
                response.data
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
                null
            }

            remoteData?.let { data ->
                emit(Resource.Success(data = data))
                emit(Resource.Loading(false))
            }
        }
    }
}