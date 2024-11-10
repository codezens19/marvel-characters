package co.diwakar.marvelcharacters.data.mapper

import co.diwakar.marvelcharacters.data.local.MarvelCharactersListingEntity
import co.diwakar.marvelcharacters.domain.model.ImageData
import co.diwakar.marvelcharacters.domain.model.MarvelCharacter

fun MarvelCharactersListingEntity.toMarvelCharactersListing(): MarvelCharacter {
    return MarvelCharacter(
        id = id,
        name = name,
        description = description,
        modified = modified,
        thumbnail = ImageData(
            path = characterImage,
            extension = imageExtension
        ),
        isComicsPresent = isComicsPresent
    )
}

fun MarvelCharacter.toMarvelCharactersListingEntity(): MarvelCharactersListingEntity {
    return MarvelCharactersListingEntity(
        id = id,
        name = name,
        description = description,
        //break the thumbnail into [characterImage] & [imageExtension]
        characterImage = thumbnail?.path,
        imageExtension = thumbnail?.extension,
        modified = modified,
        //set the [isComicsPresent] true if there is any comics available
        isComicsPresent = (comics?.available ?: 0) > 0
    )
}