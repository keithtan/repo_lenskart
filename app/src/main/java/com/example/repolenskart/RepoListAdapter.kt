package com.example.repolenskart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.repolenskart.databinding.ListItemRepoBinding

class RepoListAdapter(private val clickListener: RepoListener) : ListAdapter<Repo, RepoListAdapter.RepoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            ListItemRepoBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
        repo?.let {
            holder.bind(it, clickListener)
        }
    }

    inner class RepoViewHolder(private val binding: ListItemRepoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo, clickListener: RepoListener) {
            binding.repo = repo
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem == newItem
        }
    }
}

class RepoListener(val clickListener: (repoId: Long) -> Unit) {
    fun onClick(repo: Repo) = clickListener(repo.id)
}