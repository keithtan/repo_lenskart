package com.example.repolenskart

data class Repo(
    val id: Long,
    val name: String,
    val owner: Owner,
    var selected: Boolean = false
)

fun NetworkRepo.asDomain(): Repo {
    return Repo(id, name, owner)
}

sealed class RepoUiState {
    data class Success(val repos: List<Repo>) : RepoUiState()
    object Loading : RepoUiState()
    object Failure : RepoUiState()
}