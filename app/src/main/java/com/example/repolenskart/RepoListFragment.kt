package com.example.repolenskart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.repolenskart.databinding.FragmentRepoListBinding
import kotlinx.coroutines.flow.distinctUntilChanged
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
                .distinctUntilChanged()
                .collect {
                    binding.loading = it is RepoUiState.Loading
                    binding.failure = it is RepoUiState.Failure
                    binding.success = it is RepoUiState.Success

                    when (it) {
                        is RepoUiState.Success -> {
                            adapter.submitList(it.repos)
                        }
                        is RepoUiState.Failure -> {
                            when (it.exception) {
                                is NoNetworkException -> {
                                    noNetworkDialog()
                                }
                                is RepoException -> {
                                    genericDialog()
                                }
                            }
                        }
                        else -> Unit
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

        binding.ivRefresh.setOnClickListener {
            viewModel.getTrendingRepos()
        }

        return binding.root
    }

    private fun noNetworkDialog() {
        setFragmentResultListener(NO_NETWORK_DIAG_TAG) { _, bundle ->
            val customDialogFragmentResult = bundle.getParcelable<CustomDialogFragmentResult>(
                CustomDialogFragment.DIALOG_ACTION
            )
            customDialogFragmentResult?.run {
                when (code) {
                    CustomDialogFragment.POSITIVE_PRESSED -> {
                        viewModel.getTrendingRepos()
                    }
                }
            }
            clearFragmentResult(NO_NETWORK_DIAG_TAG)
            clearFragmentResultListener(NO_NETWORK_DIAG_TAG)
        }
        
        val bundle = CustomDialogFragmentArgs(
            ownFragmentTag = NO_NETWORK_DIAG_TAG,
            dialogTitle = getString(R.string.no_internet_title),
            contentText = getString(R.string.no_internet_message),
            positiveText = getString(R.string.button_retry),
            negativeText = getString(R.string.button_back)
        ).toBundle()
        findNavController().navigate(R.id.customDialogFragment, bundle)
    }

    private fun genericDialog() {
        setFragmentResultListener(GENERIC_ERROR_DIAG_TAG) { _, bundle ->
            val customDialogFragmentResult = bundle.getParcelable<CustomDialogFragmentResult>(
                CustomDialogFragment.DIALOG_ACTION
            )
            customDialogFragmentResult?.run {
                when (code) {
                    CustomDialogFragment.POSITIVE_PRESSED -> {
                        viewModel.getTrendingRepos()
                    }
                }
            }
            clearFragmentResult(GENERIC_ERROR_DIAG_TAG)
            clearFragmentResultListener(GENERIC_ERROR_DIAG_TAG)
        }

        val bundle = CustomDialogFragmentArgs(
            ownFragmentTag = GENERIC_ERROR_DIAG_TAG,
            dialogTitle = getString(R.string.error),
            contentText = getString(R.string.something_went_wrong),
            positiveText = getString(R.string.button_retry),
            negativeText = getString(R.string.button_back)
        ).toBundle()
        findNavController().navigate(R.id.customDialogFragment, bundle)
    }

    companion object {
        const val NO_NETWORK_DIAG_TAG = "NO_NETWORK_DIAG_TAG"
        const val GENERIC_ERROR_DIAG_TAG = "GENERIC_ERROR_DIAG_TAG"
    }

}