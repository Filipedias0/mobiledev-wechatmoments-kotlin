package com.tws.moments.data.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun displayImage(url: String?, imageView: ImageView)
}