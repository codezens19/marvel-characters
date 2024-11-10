package co.diwakar.marvelcharacters.di

import co.diwakar.marvelcharacters.data.repository.MarvelCharactersRepositoryImpl
import co.diwakar.marvelcharacters.domain.repository.MarvelCharactersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarvelCharactersListingRepository(
        marvelComRepositoryImpl: MarvelCharactersRepositoryImpl
    ): MarvelCharactersRepository
}