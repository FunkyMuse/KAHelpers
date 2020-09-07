package com.crazylegend.coroutines

import android.graphics.ImageDecoder
import kotlinx.coroutines.CoroutineScope

/**
 * Created by crazy on 9/1/20 to long live and prosper !
 */


suspend inline fun ImageDecoder.decodeImageOnIO(crossinline transform: ImageDecoder.() -> Unit) =
        withIOContext {
            transform(this@decodeImageOnIO)
        }


suspend inline fun ImageDecoder.decodeImageOnDefault(crossinline transform: ImageDecoder.() -> Unit) =
        withDefaultContext {
            transform(this@decodeImageOnDefault)
        }


inline fun ImageDecoder.decodeImageIO(scope: CoroutineScope, crossinline transform: ImageDecoder.() -> Unit) =
        scope.io {
            transform(this@decodeImageIO)
        }


inline fun ImageDecoder.decodeImageOnDefault(scope: CoroutineScope, crossinline transform: ImageDecoder.() -> Unit) =
        scope.default {
            transform(this@decodeImageOnDefault)
        }

