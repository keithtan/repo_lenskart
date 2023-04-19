package com.example.repolenskart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RepoListViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<RepoUiState> = MutableStateFlow(RepoUiState.Loading)
    val uiState: StateFlow<RepoUiState> = _uiState

    fun getTrendingRepos() {

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
                    _uiState.value = RepoUiState.Success(
                        repoList.map { it.asDomain() }
                    )
                }

            } else {
                Timber.d("fail")
            }
        }
    }
}