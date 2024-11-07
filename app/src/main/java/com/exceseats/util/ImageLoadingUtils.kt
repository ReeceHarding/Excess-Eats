package com.exceseats.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File
import javax.inject.Inject

class ImageLoadingUtils @Inject constructor(
    private val context: Context
) {
    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()

    fun loadImage(imageUrl: String?, imageView: ImageView, placeholderResId: Int) {
        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .placeholder(placeholderResId)
            .into(imageView)
    }

    fun preloadImage(imageUrl: String?) {
        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .preload()
    }

    fun clearCache() {
        Glide.get(context).clearMemory()
        Thread {
            Glide.get(context).clearDiskCache()
        }.start()
    }
}
