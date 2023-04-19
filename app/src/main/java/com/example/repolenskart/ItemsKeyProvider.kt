package com.example.repolenskart

import androidx.recyclerview.selection.ItemKeyProvider

class ItemsKeyProvider(private val adapter: RepoListAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {

    override fun getKey(position: Int): Long =
        adapter.currentList[position].id


    override fun getPosition(key: Long): Int =
        adapter.currentList.indexOfFirst { it.id == key }
}
