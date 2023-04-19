package com.example.repolenskart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.repolenskart.databinding.ListItemRepoBinding
import timber.log.Timber

class RepoListAdapter : ListAdapter<Repo, RepoListAdapter.RepoViewHolder>(DiffCallback) {

    private var selected = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            ListItemRepoBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
//        holder.itemView.isSelected = selected == position
        holder.itemView.setBackgroundColor(
            if (selected == position) Color.GREEN else Color.TRANSPARENT
        )
        repo?.let {
            holder.bind(it)
        }
    }

    inner class RepoViewHolder(private val binding: ListItemRepoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun getItem(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {

                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long = items[bindingAdapterPosition].id
            }

        fun bind(repo: Repo) {
            binding.repo = repo
            Timber.d("keith sel $selected")
//            if (selected == repo.id) {
//                binding.root.setBackgroundColor(Color.LTGRAY)
//            } else {
//                binding.root.setBackgroundColor(Color.WHITE)
//            }
            binding.setClickListener {
//                Timber.d("keith click $adapterPosition")
//                it.setBackgroundColor(Color.LTGRAY)
//                selected = repo.id
                notifyItemChanged(selected)
                selected = layoutPosition
                notifyItemChanged(selected)
            }
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