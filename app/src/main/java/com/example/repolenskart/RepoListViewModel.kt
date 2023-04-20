package com.example.repolenskart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RepoListViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<RepoUiState> = MutableStateFlow(RepoUiState.Loading)
    val uiState: StateFlow<RepoUiState> = _uiState

    private var _repoList = emptyList<Repo>()

    init {
        getTrendingRepos()
    }
    private fun getTrendingRepos() {
        Timber.d("start")

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.d("$throwable")
        }

        viewModelScope.launch(coroutineExceptionHandler + Dispatchers.IO) {

//            if (networkStateFlow.value != true) {
//                throw NoNetworkException()
//            }

            val gitHubApiService = GitHubApi.retrofitService
            val response = gitHubApiService.trendingRepos()
            if (response.isSuccessful) {
                Timber.d("${response.body()}")
                val responseRepoList = response.body()

                if (responseRepoList.isNullOrEmpty()) {
                    _uiState.value = RepoUiState.Failure
                } else {
                    val repoList = responseRepoList.map { it.asDomain() }
                    _repoList = repoList
                    _uiState.value = RepoUiState.Success(repoList)
                }

            } else {
                Timber.d("fail")
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