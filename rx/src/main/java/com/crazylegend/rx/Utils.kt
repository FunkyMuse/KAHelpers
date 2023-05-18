package com.crazylegend.rx

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import java.io.ByteArrayOutputStream

/**
 * Created by crazy on 9/1/20 to long live and prosper !
 */

fun ByteArray.toBitmap(): Single<Bitmap>? {

    return Single.fromCallable {
        BitmapFactory.decodeByteArray(this, 0, this.size)
    }.subscribeOn(computationScheduler).observeOn(mainThreadScheduler)

}

fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat, quality: Int): Single<ByteArray>? {

    val bos = ByteArrayOutputStream()

    return Single.fromCallable {
        this.compress(compressFormat, quality, bos)
        bos.toByteArray()
    }.subscribeOn(computationScheduler).observeOn(mainThreadScheduler).doAfterSuccess {
        bos.flush()
        bos.close()
    }

}


/*fun Context.cacheImage(url: String): Observable<Boolean> {
    return Observable.create {

        if (url.isEmpty()) {
            it.onNext(false)
        } else {
            Glide.with(applicationContext)
                    .downloadOnly()
                    .load(url)
                    .listener(object : RequestListener<File> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {

                            if (e != null) {
                                it.onNext(false)
                            }
                            return false

                        }

                        override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            it.onNext(true)
                            return false
                        }


                    })
                    .submit()
        }

    }
}*/
