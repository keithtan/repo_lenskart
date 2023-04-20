package com.example.repolenskart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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

        val binding = FragmentRepoListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = RepoListAdapter(RepoListener { repoId ->
            viewModel.updateSelection(repoId)
        })
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.loading = it is RepoUiState.Loading
                    when (it) {
                        is RepoUiState.Success -> {
                            adapter.submitList(it.repos)
                        }
                        RepoUiState.Loading -> {

                        }
                        RepoUiState.Failure -> {

                        }
                    }
                }
        }

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.filter(it)
                }
                return false
            }
        })

        return binding.root
    }

}