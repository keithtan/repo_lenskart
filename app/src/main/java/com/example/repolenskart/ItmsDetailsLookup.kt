package com.example.repolenskart

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView


class ItemsDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {

        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            //4
            return (recyclerView.getChildViewHolder(view) as RepoListAdapter.RepoViewHolder).getItem()
        }
        return null
    }
}
