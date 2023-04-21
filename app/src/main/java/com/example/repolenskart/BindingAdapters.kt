package com.example.repolenskart

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("avatarUrl")
fun ImageView.bindAvatar(imgUrl: String?) {
    imgUrl?.let {
        Glide.with(context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_broken_image))
            .into(this)
    }
}