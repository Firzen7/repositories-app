package net.firzen.android.learning.repositoriesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow


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



}
