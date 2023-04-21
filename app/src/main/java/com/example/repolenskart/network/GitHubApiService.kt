package com.example.repolenskart.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {

    @GET("search/repositories?sort=stars&order=desc")
    suspend fun trendingRepos(
        @Query("q", encoded = true) query: String = "stars:>1"
    ) : Response<NetworkRepoResult>
}