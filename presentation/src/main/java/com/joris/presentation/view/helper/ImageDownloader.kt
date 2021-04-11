package com.joris.presentation.view.helper

import android.widget.ImageView
import com.bumptech.glide.Glide


object ImageDownloader {
    fun loadBitmapIntoImageView(imageView: ImageView, url: String?) {
        Glide
            .with(imageView)
            .load(url)
            .into(imageView);
    }
}