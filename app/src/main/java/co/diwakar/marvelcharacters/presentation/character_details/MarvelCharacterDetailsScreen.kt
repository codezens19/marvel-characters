package co.diwakar.marvelcharacters.presentation.character_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.diwakar.marvelcharacters.R
import co.diwakar.marvelcharacters.domain.model.MarvelCharacter
import co.diwakar.marvelcharacters.presentation.comics.ComicsComposable
import co.diwakar.marvelcharacters.ui.composables.CharacterImage
import co.diwakar.marvelcharacters.ui.composables.MediumHeader
import co.diwakar.marvelcharacters.ui.theme.TextDark
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun MarvelCharacterDetailsScreen(
    marvelCharacter: MarvelCharacter,
    navigator: DestinationsNavigator,
    viewModel: MarvelCharacterDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    state.errorMessage?.let { message ->
        rememberCoroutineScope().launch {
            scaffoldState.snackbarHostState.showSnackbar(message)
            viewModel.onEvent(MarvelCharacterDetailsEvent.ResetError)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        val lazyListState = rememberLazyListState()
        var scrolledY = 0f
        var previousOffset = 0

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            item {
                Modifier.fillMaxWidth()
                ImageAndNameComposable(
                    character = marvelCharacter,
                    onBackClicked = {
                        navigator.popBackStack()
                    },
                    modifier = Modifier.graphicsLayer {
                        scrolledY += lazyListState.firstVisibleItemScrollOffset - previousOffset
                        translationY = scrolledY * 0.1f
                        previousOffset = lazyListState.firstVisibleItemScrollOffset
                    }
                )
            }
            item {
                DescriptionComposable(description = marvelCharacter.description)
            }
            if (marvelCharacter.isComicsPresent()) {
                item {
                    MediumHeader(
                        header = "Comics",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .absolutePadding(top = 16.dp, bottom = 8.dp),
                        fontSize = 20.sp
                    )
                }
                item {
                    ComicsComposable(
                        comics = state.comics,
                        loadNextPage = {
                            marvelCharacter.id?.let { characterId ->
                                viewModel.onEvent(
                                    MarvelCharacterDetailsEvent.FetchNextPage(characterId)
                                )
                            }
                        },
                        isLoading = state.isLoading,
                        progressCount = state.progressCount()
                    )
                }
            }
        }
    }
}

@Composable
fun ImageAndNameComposable(
    character: MarvelCharacter,
    onBackClicked: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .aspectRatio(1f)
    ) {
        CharacterImage(
            path = character.thumbnail?.getCompletePath(),
            modifier = Modifier
                .fillMaxSize()
        )
        NameComposable(
            character.name ?: "",
            onBackClicked = onBackClicked
        )
    }
}

@Composable
fun NameComposable(
    name: String?,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0F to Color.Black.copy(alpha = 0.8F),
                    .5F to Color.Black.copy(alpha = 0.5F),
                    1F to Color.Transparent
                )
            )
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
                tint = Color.White
            )
        }
        Text(
            text = name ?: "",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(12.dp)
                .heightIn(16.dp)
        )
    }
}

@Composable
fun DescriptionComposable(description: String?) {
    if (description.isNullOrBlank().not()) {
        Text(
            text = "$description",
            color = TextDark,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
