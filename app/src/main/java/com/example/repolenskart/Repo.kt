package com.example.repolenskart

data class Repo(
    val id: Long,
    val name: String
)

fun NetworkRepo.asDomain() = Repo(
    id = id,
    name = name
)

sealed class RepoUiState {
    data class Success(val repos: List<Repo>) : RepoUiState()
    object Loading : RepoUiState()
    object Failure : RepoUiState()
}