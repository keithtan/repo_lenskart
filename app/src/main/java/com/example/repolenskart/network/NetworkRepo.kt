package com.example.repolenskart.network

data class NetworkRepoResult(
    val items: List<NetworkRepo>
)
data class NetworkRepo(
    val id: Long,
    val name: String,
    val owner: Owner
)

data class Owner(
    val avatar_url: String
)