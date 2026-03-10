package net.firzen.android.learning.repositoriesapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems


@Composable
fun RepositoriesScreen(repos: LazyPagingItems<Repository>) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ) {
        items(repos.itemCount) { index ->
            val repo = repos[index]
            if (repo != null) {
                RepositoryItem(index, repo)
            }
        }

        handleInitialLoadStates(repos)
    }
}

/**
 * Handles initial states that occur after the first request of paginated items or after
 * a refresh event.
 *
 * LoadState.Loading --> shows progress bar
 * LoadState.Error   --> shows error messge with retry button
 */
private fun LazyListScope.handleInitialLoadStates(repos: LazyPagingItems<Repository>) {
    val refreshLoadState = repos.loadState.refresh

    when(refreshLoadState) {
        // this happens when items are being loaded by Paging library
        is LoadState.Loading -> {
            // `item()` adds individual item into `LazyColumn`
            item {
                // in out case, this item will take whole screen, thanks to
                // `Modifier.fillParentMaxSize()`
                LoadingItem(
                    Modifier.fillParentMaxSize()
                )
            }
        }

        // this is called when initial items could not be loaded at all
        is LoadState.Error -> {
            // LoadState.Error carries a Throwable, so we get it here
            val exception = refreshLoadState.error

            // and here we add a fullscreen item with error message
            item {
                ErrorItem(
                    message = exception.message ?: "",
                    modifier = Modifier.fillParentMaxSize(),
                    // retrying initial loading is natively supported by Paging library! <3
                    retry = { repos.retry() }
                )
            }
        }

        is LoadState.NotLoading -> null
    }
}

@Composable
fun ErrorItem(message: String, modifier: Modifier, retry: () -> Unit) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 2,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Red
        )
        Button(
            onClick = retry,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Try again")
        }
    }
}

@Composable
fun LoadingItem(modifier: Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun RepositoryItem(index: Int, item: Repository) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.padding(8.dp).height(120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp))
            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge)
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3)
            }
        }
    }
}
