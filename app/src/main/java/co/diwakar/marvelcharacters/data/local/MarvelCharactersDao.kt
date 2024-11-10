package co.diwakar.marvelcharacters.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarvelCharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarvelCharactersListings(
        marvelCharactersListingEntities: List<MarvelCharactersListingEntity>
    )

    @Query("DELETE FROM marvelcharacterslistingentity")
    suspend fun clearMarvelCharactersListing()

    @Query("SELECT * FROM marvelcharacterslistingentity ORDER by name")
    suspend fun getMarvelCharactersListing(): List<MarvelCharactersListingEntity>
}