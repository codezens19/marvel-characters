package co.diwakar.marvelcharacters.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
fun CharacterImage(path: String?, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = path,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading,
            is AsyncImagePainter.State.Error ->
                ShimmerAnimation(modifier = Modifier.fillMaxSize())
            else -> SubcomposeAsyncImageContent()
        }
    }
}