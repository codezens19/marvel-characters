package co.diwakar.marvelcharacters.presentation.characters_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.diwakar.marvelcharacters.domain.model.MarvelCharacter
import co.diwakar.marvelcharacters.presentation.destinations.MarvelCharacterDetailsScreenDestination
import co.diwakar.marvelcharacters.ui.composables.ShimmerAnimation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun MarvelCharactersListingScreen(
    navigator: DestinationsNavigator,
    viewModel: MarvelCharactersListingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )
    val scaffoldState = rememberScaffoldState()
    state.errorMessage?.let { message ->
        rememberCoroutineScope().launch {
            scaffoldState.snackbarHostState.showSnackbar(message)
            viewModel.onEvent(MarvelCharactersListingEvent.ResetError)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchCompose(
                state.searchQuery,
                onValueChanged = {
                    viewModel.onEvent(
                        MarvelCharactersListingEvent.OnSearchQueryChange(it)
                    )
                }
            )
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(MarvelCharactersListingEvent.Refresh)
                }
            ) {
                CharactersList(
                    characters = state.characters,
                    isLoading = state.isLoading,
                    progressCount = state.progressCount(),
                    loadNextPage = {
                        viewModel.onEvent(MarvelCharactersListingEvent.FetchNextPage)
                    },
                    onItemClicked = {
                        navigator.navigate(MarvelCharacterDetailsScreenDestination(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchCompose(searchQuery: String?, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        value = searchQuery ?: "",
        onValueChange = onValueChanged,
        placeholder = {
            Text(text = "Search Characters...")
        },
        maxLines = 1,
        singleLine = true
    )
}

@Composable
private fun CharactersList(
    characters: List<MarvelCharacter>,
    isLoading: Boolean,
    progressCount: Int,
    loadNextPage: () -> Unit,
    onItemClicked: (MarvelCharacter) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        items(characters.size) { index ->
            if (index >= characters.size - 1) {
                loadNextPage()
            }
            val character = characters[index]
            MarvelCharacterItem(
                character = character,
                modifier = Modifier.clickable {
                    onItemClicked(character)
                }
            )
        }
        progressCompose(isLoading = isLoading, count = progressCount)
    }
}

private fun LazyGridScope.progressCompose(isLoading: Boolean, count: Int) {
    items(count) {
        if (isLoading) {
            ShimmerAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .aspectRatio(0.8f)
                    .clip(CutCornerShape(bottomEnd = 16.dp))
            )
        }
    }
}