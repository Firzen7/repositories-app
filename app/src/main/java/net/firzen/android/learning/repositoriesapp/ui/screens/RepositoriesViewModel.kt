package net.firzen.android.learning.repositoriesapp.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import net.firzen.android.learning.repositoriesapp.paging.RepositoriesPagingSource
import net.firzen.android.learning.repositoriesapp.data.Repository
import net.firzen.android.learning.repositoriesapp.domain.CustomCountdown


class RepositoriesViewModel(private val reposPagingSource:
                            RepositoriesPagingSource = RepositoriesPagingSource()) : ViewModel() {

    val repositories: Flow<PagingData<Repository>> =
        Pager(
            // defines size of chunks Paging library will be asking API for (in our case
            // it will be 20 items per chunk)
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { reposPagingSource }
            // cachedIn keeps paginated content cached as long as the given coroutine scope is
            // alive; in our case it will be as long as this ViewModel is kept, so paginated
            // data will survive configuration changes
        ).flow.cachedIn(viewModelScope)

    // state holding current message about the timer progress
    val timerState = mutableStateOf("")

    // the timer for determining if users used the app for long enough to win the prize
    var timer = CustomCountdown(
        onTick = { msLeft ->
            timerState.value = (msLeft / 1000).toString() + " seconds left"
        },
        onFinish = {
            timerState.value = "You won a prize!"
        }
    )

    override fun onCleared() {
        super.onCleared()
        // stops the timer when this ViewModel is destroyed
        timer.stop()
    }
}
