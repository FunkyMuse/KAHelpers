package com.crazylegend.kotlinextensions.imagedecoder

import android.content.ContentResolver
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
fun ImageDecoder.Source.decodeBitmap() =  ImageDecoder.decodeBitmap(this)


@RequiresApi(Build.VERSION_CODES.P)
fun Resources.decodeImage(@IdRes img:Int ) = ImageDecoder.createSource(this, img)

@RequiresApi(Build.VERSION_CODES.P)
fun ContentResolver.decodeImage(uri:Uri) = ImageDecoder.createSource(this, uri)

@RequiresApi(Build.VERSION_CODES.P)
fun AssetManager.decodeImage(fileName:String) = ImageDecoder.createSource(this, fileName)

@RequiresApi(Build.VERSION_CODES.P)
fun Drawable.startAnimatedDrawable(){
    if (this is AnimatedImageDrawable){
        this.start()
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun Drawable.stopAnimatedDrawable(){
    if (this is AnimatedImageDrawable){
        this.stop()
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.crop(size:Int){
    ImageDecoder.OnHeaderDecodedListener { decoder, info, _ ->
        val centerX = info.size.width / 2
        val centerY = info.size.height / 2
        decoder.crop = Rect(centerX-size, centerY-size,centerX+size, centerY+size)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.resize(width:Int, height:Int){
    ImageDecoder.OnHeaderDecodedListener { decoder, _, _ ->
        decoder.setTargetSize(width, height)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.roundCorners(xcornerRadius:Float, yCornerRadius:Float){
    ImageDecoder.OnHeaderDecodedListener { decoder, _, _ ->
        val path = Path().apply {
            fillType = Path.FillType.INVERSE_EVEN_ODD
        }
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
        decoder.setPostProcessor { canvas ->
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val direction = Path.Direction.CW
            path.addRoundRect(0f, 0f, width, height, xcornerRadius, yCornerRadius, direction)
            canvas.drawPath(path, paint)
            PixelFormat.TRANSLUCENT
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.errorListenerCreatePartiallyDecodedImage(callback: (errorCode:Int) -> Unit = {_->}){
    ImageDecoder.OnHeaderDecodedListener { decoder, _, _->
        decoder.setOnPartialImageListener { decodeException ->

            when(decodeException.error){
                ImageDecoder.DecodeException.SOURCE_EXCEPTION->{
                    callback(ImageDecoder.DecodeException.SOURCE_EXCEPTION)
                }

                ImageDecoder.DecodeException.SOURCE_INCOMPLETE->{
                    callback(ImageDecoder.DecodeException.SOURCE_INCOMPLETE)
                }

                ImageDecoder.DecodeException.SOURCE_MALFORMED_DATA->{
                    callback(ImageDecoder.DecodeException.SOURCE_MALFORMED_DATA)
                }
            }

            true
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
fun ImageDecoder.errorListenerStopTheProcess(callback: (errorCode:Int) -> Unit = {_->}){
    ImageDecoder.OnHeaderDecodedListener { decoder, _, _->
        decoder.setOnPartialImageListener { decodeException ->

            when(decodeException.error){
                ImageDecoder.DecodeException.SOURCE_EXCEPTION->{
                    callback(ImageDecoder.DecodeException.SOURCE_EXCEPTION)
                }

                ImageDecoder.DecodeException.SOURCE_INCOMPLETE->{
                    callback(ImageDecoder.DecodeException.SOURCE_INCOMPLETE)
                }

                ImageDecoder.DecodeException.SOURCE_MALFORMED_DATA->{
                    callback(ImageDecoder.DecodeException.SOURCE_MALFORMED_DATA)
                }
            }

            false
        }
    }
}

fun ImageDecoder.decodeImageOnWorkerthread(transform : ImageDecoder.()-> Unit){
    GlobalScope.launch(Dispatchers.Default){
        transform.invoke(this@decodeImageOnWorkerthread)
    }
}

