package co.diwakar.marvelcharacters.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MarvelCharactersListingEntity::class],
    version = 1
)
abstract class MarvelCharacterDatabase : RoomDatabase() {
    abstract val dao: MarvelCharactersDao
}