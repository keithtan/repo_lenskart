package com.example.repolenskart

data class NetworkRepo(
    val id: Long,
    val name: String,
    val owner: Owner
)

data class Owner(
    val avatar_url: String
)