package co.diwakar.marvelcharacters.di

import android.app.Application
import androidx.room.Room
import co.diwakar.marvelcharacters.BuildConfig
import co.diwakar.marvelcharacters.data.local.MarvelCharacterDatabase
import co.diwakar.marvelcharacters.data.remote.MarvelApi
import co.diwakar.marvelcharacters.data.remote.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMarvelApi(retrofit: Retrofit): MarvelApi {
        return retrofit.create(MarvelApi::class.java)
    }

    @Provides
    @Named("marvel-characters-base-url")
    fun providesBaseUrl(): String = MarvelApi.BASE_URL

    @Provides
    @Singleton
    fun providesRetrofit(
        @Named("marvel-characters-base-url") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(authInterceptor)
            addInterceptor(loggingInterceptor)
        }.build()

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideMarvelCharacterDatabase(app: Application): MarvelCharacterDatabase {
        return Room.databaseBuilder(
            app,
            MarvelCharacterDatabase::class.java,
            "marveldb.db"
        ).build()
    }
}