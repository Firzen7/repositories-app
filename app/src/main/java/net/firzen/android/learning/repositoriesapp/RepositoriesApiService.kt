package net.firzen.android.learning.repositoriesapp

import retrofit2.http.GET
import retrofit2.http.Query


interface RepositoriesApiService {

    @GET("repositories?q=mobile&sort=stars&per_page=20")
    // @Query annotation allows for variable http parameters
    suspend fun getRepositories(@Query("page") page: Int) : RepositoriesResponse

}
