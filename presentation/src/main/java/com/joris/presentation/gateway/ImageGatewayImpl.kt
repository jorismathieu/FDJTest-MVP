package com.joris.presentation.gateway

import android.widget.ImageView
import com.bumptech.glide.Glide

interface ImageGateway {
    fun loadImage(imageView: ImageView, url: String?)
}

class ImageGatewayImpl: ImageGateway {
    override fun loadImage(imageView: ImageView, url: String?) {
        Glide
            .with(imageView)
            .load(url)
            .into(imageView);
    }
}