package net.firzen.android.learning.repositoriesapp

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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import net.firzen.android.learning.repositoriesapp.ui.theme.RepositoriesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepositoriesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: RepositoriesViewModel = viewModel()
                    val reposFlow = viewModel.repositories

                    // `collectAsLazyPagingItems()` can consume and remember the paginated data
                    // from within reposFlow
                    val lazyRepoItems: LazyPagingItems<Repository> = reposFlow.collectAsLazyPagingItems()

                    Box(Modifier.padding(innerPadding)) {
                        RepositoriesScreen(lazyRepoItems)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val dummyRepositories = listOf(
        Repository("abc", "Repo 1", "This is test repository"),
        Repository("k6c", "Repo 2", "This is test repository"),
        Repository("7hc", "Repo 3", "This is test repository"),
        Repository("54g", "Repo 4", "This is test repository"),
        Repository("fgh", "Repo 5", "This is test repository"),
        )

    val pagingItems = flowOf(PagingData.from(dummyRepositories)).collectAsLazyPagingItems()

    RepositoriesAppTheme {
        RepositoriesScreen(pagingItems)
    }
}