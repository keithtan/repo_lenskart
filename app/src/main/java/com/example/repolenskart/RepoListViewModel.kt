package com.example.repolenskart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RepoListViewModel : ViewModel() {

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

            } else {
                Timber.d("fail")
            }
        }
    }
}