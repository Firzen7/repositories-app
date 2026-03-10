package net.firzen.android.learning.repositoriesapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
