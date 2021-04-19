package com.joris.presentation.gateway

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

interface ImageGateway {
    fun loadImage(
        imageView: ImageView,
        placeHolder: Int,
        url: String?
    )
}

class ImageGatewayImpl : ImageGateway {
    override fun loadImage(
        imageView: ImageView,
        placeHolder: Int,
        url: String?
    ) {

        val placeHolderRequest: RequestBuilder<Drawable> = Glide.with(imageView)
            .load(placeHolder)
            .fitCenter()

        Glide
            .with(imageView)
            .load(url)
            .thumbnail(placeHolderRequest)
            .into(imageView)
    }
}