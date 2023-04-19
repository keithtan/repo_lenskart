package com.example.repolenskart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.repolenskart.databinding.FragmentRepoListBinding
import kotlinx.coroutines.launch

class RepoListFragment : Fragment() {

    private val viewModel: RepoListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.getTrendingRepos()

        val binding = FragmentRepoListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = RepoListAdapter()
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is RepoUiState.Success -> {
                            adapter.submitList(it.repos)
                        }
                        is RepoUiState.Loading -> {

                        }
                        is RepoUiState.Failure -> {

                        }
                    }
                }
        }

        return binding.root
    }

}