package com.example.repolenskart

import retrofit2.Response
import retrofit2.http.GET

interface GitHubApiService {

    @GET("repositories?sort=stars&order=desc")
    suspend fun trendingRepos() : Response<List<NetworkRepo>>
}