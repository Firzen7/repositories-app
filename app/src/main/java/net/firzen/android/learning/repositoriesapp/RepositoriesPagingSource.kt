package net.firzen.android.learning.repositoriesapp

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * Defines the source of data for the paginated content and decides which page to request.
 * In this app, it loads data from your remote Github API.
 *
 * Inherits from `PagingSource`, which is part of Paging library. The meaning of syntax is:
 *  PagingSource<Int, Repository> --> PagingSource<PAGE_INDEX, LOADED_DATA>
 */
class RepositoriesPagingSource(private val restInterface: RepositoriesApiService
    = DependencyContainer.repositoriesRetrofitClient) : PagingSource<Int, Repository>() {

    /**
     * The load() is called automatically by the Paging library and should fetch more items
     * asynchronously. This method takes in a LoadParams object that keeps track
     * of information such as what is the key (index) of the page that must be requested,
     * or the initial load size of items. Also, this method returns a LoadResult object
     * indicating if a specific query result was successful or has failed.
     */
    override suspend fun load(params: LoadParams<Int>) : LoadResult<Int, Repository> {
        try {
            val nextPage = params.key ?: 1
            val repos = restInterface.getRepositories(nextPage).repos

            return LoadResult.Page(
                // data are our Repository entities loaded from Github API
                data = repos,
                // previous key is null if this is first page (there is then no page preceding)
                prevKey = if (nextPage == 1) null else nextPage - 1,
                // next key is always incremented (I think this is ignoring the fact that
                // not even Github is infinite, but anyway.. this should be handled by try-catch)
                nextKey = nextPage + 1)

        } catch (e: Exception) {
            // load result should contain exception in case of error
            return LoadResult.Error(e)
        }
    }

    /**
     * The getRefreshKey() is called to obtain and return the most recent page key in case
     * of a refresh event so that the user is returned to the latest known page (and not the first
     * one). A refresh event can come from a variety of sources, such as a manual UI refresh
     * triggered by the user, a database cache invalidation event, system events, and so on.
     */
    override fun getRefreshKey(state: PagingState<Int, Repository>) : Int? {
        // in our case, we will not use refresh capabilities, so we can just return null
        return null
    }
}
