package co.diwakar.marvelcharacters.presentation.characters_listings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.diwakar.marvelcharacters.domain.model.MarvelCharacter
import co.diwakar.marvelcharacters.ui.composables.CharacterImage
import co.diwakar.marvelcharacters.ui.theme.Marvel

@Composable
fun MarvelCharacterItem(
    character: MarvelCharacter,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(0.8f)
            .clip(CutCornerShape(bottomEnd = 16.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            CharacterImage(
                path = character.thumbnail?.getCompletePath(),
                modifier = Modifier.weight(1f)
            )
            Divider(
                color = Marvel, modifier = Modifier.fillMaxWidth(), thickness = 2.dp
            )
            Text(
                text = character.name ?: "",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(12.dp)
                    .heightIn(16.dp)
            )
        }
    }
}