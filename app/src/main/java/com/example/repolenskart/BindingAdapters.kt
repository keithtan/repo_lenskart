package com.example.repolenskart

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("avatarUrl")
fun ImageView.bindAvatar(imgUrl: String?) {
    imgUrl?.let {
//        val fullUrl = "https://image.tmdb.org/t/p/w154$it"
//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUrl)
//            .apply(RequestOptions()
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.ic_broken_image))
//            .transform(CenterCrop(), RoundedCorners(25))
            .into(this)
    }
}