package net.firzen.android.learning.repositoriesapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import net.firzen.android.learning.repositoriesapp.ui.screens.RepositoriesScreen
import net.firzen.android.learning.repositoriesapp.ui.screens.RepositoriesViewModel
import net.firzen.android.learning.repositoriesapp.data.Repository
import net.firzen.android.learning.repositoriesapp.ui.screens.RepositoriesScreenPreview
import net.firzen.android.learning.repositoriesapp.ui.theme.RepositoriesAppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        enableEdgeToEdge()
        setContent {
            RepositoriesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: RepositoriesViewModel = viewModel()
                    val reposFlow = viewModel.repositories

                    // `collectAsLazyPagingItems()` can consume and remember the paginated data
                    // from within reposFlow
                    val lazyRepoItems: LazyPagingItems<Repository> = reposFlow.collectAsLazyPagingItems()

                    // here we get current description of the timer
                    val timerText = viewModel.timerState.value

                    Box(Modifier.padding(innerPadding)) {
                        RepositoriesScreen(
                            repos = lazyRepoItems,
                            timerText = timerText,
                            getTimer = { viewModel.timer },
                            onPauseTimer = { viewModel.timer.stop() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    RepositoriesScreenPreview()
}
