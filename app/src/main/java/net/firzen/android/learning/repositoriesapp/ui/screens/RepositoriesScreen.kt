package net.firzen.android.learning.repositoriesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import net.firzen.android.learning.repositoriesapp.data.Repository
import net.firzen.android.learning.repositoriesapp.domain.CustomCountdown
import timber.log.Timber


@Composable
fun RepositoriesScreen(repos: LazyPagingItems<Repository>,
                       timerText: String,
                       getTimer: () -> CustomCountdown,
                       onPauseTimer: () -> Unit) {

    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ) {
        item {
            CountdownItem(timerText, getTimer, onPauseTimer)
        }

        items(repos.itemCount) { index ->
            val repo = repos[index]
            if (repo != null) {
                RepositoryItem(index, repo)
            }
        }

        handleInitialLoadStates(repos)
        handleAppendLoadStates(repos)
    }
}

@Composable
private fun CountdownItem(timerText: String,
                          getTimer: () -> CustomCountdown,
                          onPauseTimer: () -> Unit) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    DisposableEffect(key1 = lifecycleOwner) {
        // called when `CountdownItem` is visible on screen (that also means user needs to scroll
        // to the very top!) --> called when composable enters composition
        Timber.i("Observer added")

        // this causes the timer to be informed about `onResume()` being called, which also
        // causes the timer to either start or continue
        lifecycle.addObserver(getTimer())

        onDispose {
            // called whenever the `CountdownItem` is NOT visible on screen - even just scrolling
            // down will trigger this block --> called when composable leaves composition

            Timber.i("Observer removed")
            onPauseTimer()    // timer is paused when `CountdownItem` is not visible
            lifecycle.removeObserver(getTimer())
        }
    }

    // shows message produced by the timer
    Text(timerText)
}

/**
 * Handles states that occur at the end of a subsequent request of paginated items.
 *
 * LoadState.Loading --> shows progress bar at the bottom of the screen
 *                       (added as last item into LazyColumn)
 * LoadState.Error   --> shows error message with retry button (also at the bottom of LazyColumn)
 */
private fun LazyListScope.handleAppendLoadStates(repos: LazyPagingItems<Repository>) {
    val appendLoadState = repos.loadState.append

    when(appendLoadState) {
        // this happens when new items are being loaded by Paging library
        is LoadState.Loading -> {
            // `item()` adds individual item into `LazyColumn`
            item {
                LoadingItem(
                    Modifier.fillMaxWidth()
                )
            }
        }

        // this is called when new items requested could not be loaded at all
        is LoadState.Error -> {
            // LoadState.Error carries a Throwable, so we get it here
            val exception = appendLoadState.error

            // and here we add an item with error message (not fullscreen this time - therefore
            // using `fillMaxWidth()` Modifier
            item {
                ErrorItem(
                    message = exception.message ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    // retrying initial loading is natively supported by Paging library! <3
                    retry = { repos.retry() }
                )
            }
        }

        is LoadState.NotLoading -> null
    }
}

/**
 * Handles initial states that occur after the first request of paginated items or after
 * a refresh event.
 *
 * LoadState.Loading --> shows fullscreen progress bar
 * LoadState.Error   --> shows fullscreen error message with retry button
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
