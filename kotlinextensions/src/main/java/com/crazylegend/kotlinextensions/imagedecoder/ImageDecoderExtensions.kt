package com.crazylegend.kotlinextensions.imagedecoder

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
import com.crazylegend.kotlinextensions.coroutines.default
import com.crazylegend.kotlinextensions.coroutines.io
import com.crazylegend.kotlinextensions.coroutines.withDefaultContext
import com.crazylegend.kotlinextensions.coroutines.withIOContext
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.nio.ByteBuffer


/**
 * Created by hristijan on 4/1/19 to long live and prosper !
 */


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

suspend inline fun ImageDecoder.decodeImageOnIO(crossinline transform: ImageDecoder.() -> Unit) {
    withIOContext {
        transform(this@decodeImageOnIO)
    }
}

suspend inline fun ImageDecoder.decodeImageOnDefault(crossinline transform: ImageDecoder.() -> Unit) {
    withDefaultContext {
        transform(this@decodeImageOnDefault)
    }
}

inline fun ImageDecoder.decodeImageIO(scope: CoroutineScope, crossinline transform: ImageDecoder.() -> Unit) {
    scope.io {
        transform(this@decodeImageIO)
    }
}


inline fun ImageDecoder.decodeImageOnDefault(scope: CoroutineScope, crossinline transform: ImageDecoder.() -> Unit) {
    scope.default {
        transform(this@decodeImageOnDefault)
    }
}

