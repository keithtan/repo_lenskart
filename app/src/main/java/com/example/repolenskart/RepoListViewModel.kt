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
                val repoList = response.body()

                if (repoList.isNullOrEmpty()) {
                    _uiState.value = RepoUiState.Failure
                } else {
                    _uiState.value = RepoUiState.Success(repoList.map { it.asDomain() })
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
                    RepoUiState.Success(it.repos.map {  repo ->
                        if (repo.id == repoId) {
                            repo.copy(selected = !repo.selected)
                        } else {
                            repo
                        }
                    })
                }
                else -> it
            }
        }
    }
}