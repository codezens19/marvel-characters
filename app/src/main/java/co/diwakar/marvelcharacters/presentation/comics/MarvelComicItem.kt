package co.diwakar.marvelcharacters.presentation.comics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.diwakar.marvelcharacters.domain.model.MarvelComic
import co.diwakar.marvelcharacters.ui.composables.CharacterImage
import co.diwakar.marvelcharacters.ui.theme.TextDark

@Composable
fun MarvelComicItem(
    comic: MarvelComic,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            CharacterImage(
                path = comic.thumbnail?.getCompletePath(),
                modifier = Modifier.weight(0.8f)
            )
            Text(
                text = comic.title ?: "",
                color = TextDark,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(top = 4.dp)
            )
        }
    }
}