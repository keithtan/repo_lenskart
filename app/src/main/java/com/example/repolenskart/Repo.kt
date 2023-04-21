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
    data class Failure(val exception: Exception) : RepoUiState()
}

class RepoException(val code: String) : Exception() {
    companion object {
        const val ERROR_REPO_EMPTY_RESPONSE = "ERROR_REPO_EMPTY_RESPONSE"
        const val ERROR_REPO_GENERIC_ERROR = "ERROR_REPO_GENERIC_ERROR"
    }
}