package com.funkymuse.kotlinextensions.imagedecoder

import android.content.ContentResolver
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import java.io.File
import java.nio.ByteBuffer


@RequiresApi(Build.VERSION_CODES.P)
fun File.decodeImage() = ImageDecoder.createSource(this)


@RequiresApi(Build.VERSION_CODES.P)
fun ByteBuffer.decodeImage() = ImageDecoder.createSource(this)


@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.Source.decodeDrawable() = ImageDecoder.decodeDrawable(this)


@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.Source.decodeBitmap() = ImageDecoder.decodeBitmap(this)


@RequiresApi(Build.VERSION_CODES.P)
fun Resources.decodeImage(@IdRes img: Int) = ImageDecoder.createSource(this, img)

@RequiresApi(Build.VERSION_CODES.P)
fun ContentResolver.decodeImage(uri: Uri) = ImageDecoder.createSource(this, uri)

@RequiresApi(Build.VERSION_CODES.P)
fun AssetManager.decodeImage(fileName: String) = ImageDecoder.createSource(this, fileName)

@RequiresApi(Build.VERSION_CODES.P)
fun Drawable.startAnimatedDrawable() {
    if (this is AnimatedImageDrawable) {
        this.start()
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun Drawable.stopAnimatedDrawable() {
    if (this is AnimatedImageDrawable) {
        this.stop()
    }
}

