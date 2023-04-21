package com.example.repolenskart.repo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repolenskart.NoNetworkException
import com.example.repolenskart.isNetworkAvailable
import com.example.repolenskart.network.GitHubApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RepoListViewModel(val app: Application) : AndroidViewModel(app) {

    private val _uiState: MutableStateFlow<RepoUiState> = MutableStateFlow(RepoUiState.Loading)
    val uiState: StateFlow<RepoUiState> = _uiState

    private var _repoList = emptyList<Repo>()

    init {
        getTrendingRepos()
    }
    fun getTrendingRepos() {
        Timber.d("start")

        _uiState.value = RepoUiState.Loading

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.d("$throwable")
            _uiState.value = when (throwable) {
                is NoNetworkException -> {
                    RepoUiState.Failure(throwable)
                }
                is RepoException -> {
                    RepoUiState.Failure(throwable)
                }
                else -> {
                    RepoUiState.Failure(RepoException(RepoException.ERROR_REPO_GENERIC_ERROR))
                }
            }
        }

        viewModelScope.launch(coroutineExceptionHandler + Dispatchers.IO) {

            if (!isNetworkAvailable(app)) {
                throw NoNetworkException()
            }

            val gitHubApiService = GitHubApi.retrofitService
            val response = gitHubApiService.trendingRepos()
            if (response.isSuccessful) {
                Timber.d("${response.body()}")
                val responseRepoList = response.body()

                if (responseRepoList.isNullOrEmpty()) {
                    throw RepoException(RepoException.ERROR_REPO_EMPTY_RESPONSE)
                } else {
                    val repoList = responseRepoList.map { it.asDomain() }
                    _repoList = repoList
                    _uiState.value = RepoUiState.Success(repoList)
                }

            } else {
                throw RepoException(RepoException.ERROR_REPO_GENERIC_ERROR)
            }
        }
    }

    fun updateSelection(repoId: Long) {
        _uiState.update {
            when (it) {
                is RepoUiState.Success -> {
                    _repoList = _repoList.map { repo ->
                        if (repo.id == repoId) {
                            repo.copy(selected = !repo.selected)
                        } else {
                            repo
                        }
                    }
                    val list = it.repos.map { repo ->
                        if (repo.id == repoId) {
                            repo.copy(selected = !repo.selected)
                        } else {
                            repo
                        }
                    }
                    RepoUiState.Success(list)
                }
                else -> it
            }
        }
    }

    fun filter(newText: String) {
        _uiState.update {
            when (it) {
                is RepoUiState.Success -> {
                    val list = _repoList.filter { repo ->
                        repo.name.contains(newText)
                    }
                    RepoUiState.Success(list)
                }
                else -> it
            }
        }
    }
}