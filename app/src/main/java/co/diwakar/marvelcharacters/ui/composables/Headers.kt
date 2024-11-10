package co.diwakar.marvelcharacters.ui.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import co.diwakar.marvelcharacters.ui.theme.TextDark

@Composable
fun MediumHeader(
    header: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    color: Color = TextDark
) {
    Text(
        text = header,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}