package co.diwakar.marvelcharacters.presentation.comics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.diwakar.marvelcharacters.domain.model.MarvelComic
import co.diwakar.marvelcharacters.ui.composables.ShimmerAnimation


@Composable
fun ComicsComposable(
    comics: List<MarvelComic>,
    loadNextPage: () -> Unit,
    isLoading: Boolean,
    progressCount: Int
) {
    LazyRow(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
    ) {
        items(comics.size) { index ->
            if (index >= comics.size - 1) {
                loadNextPage()
            }
            val isFirst = index == 0
            val isLast = index == comics.size - 1
            MarvelComicItem(
                comic = comics[index],
                modifier = Modifier
                    .absolutePadding(
                        left = if (isFirst) 12.dp else 6.dp,
                        right = if (isLast) 12.dp else 6.dp
                    )
                    .padding(vertical = 8.dp)
                    .aspectRatio(0.5f)
            )
        }
        if (isLoading) {
            items(progressCount) { index ->
                val isFirst = index == 0
                val isLast = index == progressCount - 1
                ShimmerAnimation(
                    modifier = Modifier
                        .absolutePadding(
                            left = if (isFirst) 12.dp else 6.dp,
                            right = if (isLast) 12.dp else 6.dp
                        )
                        .padding(vertical = 8.dp)
                        .aspectRatio(0.5f)
                )
            }
        }
    }
}