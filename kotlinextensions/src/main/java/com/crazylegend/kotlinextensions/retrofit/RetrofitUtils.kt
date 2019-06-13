package com.crazylegend.kotlinextensions.retrofit

import com.crazylegend.kotlinextensions.color.randomColor
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.retrofit.withProgress.OnAttachmentDownloadListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.ByteString
import retrofit2.Retrofit
import java.io.File


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */


inline fun <reified T> Retrofit?.create(): T? {
    return this?.create(T::class.java)
}

/* example
private  val retrofit by lazy {
    RetrofitClient.gsonInstanceRxJava(context, BASE_URL).create<RetrofitInterface>()
}*/

/**
 *
 * handle api call dsl
 * USAGE
 *
 *
retrofitResult.handle({
//loading

}, {
// no data from server

}, {
//empty data

}, { // call error
message, throwable, exception ->

}, { // api error
errorBody, responseCode ->

}, {
//success handle

})
 *
 *
 */

fun <T> RetrofitResult<T>.handle(
        loading: () -> Unit,
        noData: () -> Unit,
        emptyData: () -> Unit,
        calError: (message: String, throwable: Throwable, exception: Exception?) -> Unit = { _, _, _ -> },
        apiError: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
        success: T.() -> Unit) {

    when (this) {
        is RetrofitResult.Success -> {
            success.invoke(value)
        }
        RetrofitResult.Loading -> {
            loading()
        }
        RetrofitResult.NoData -> {
            noData()
        }
        RetrofitResult.EmptyData -> {
            emptyData()
        }
        is RetrofitResult.Error -> {
            calError(message, throwable, exception)
        }
        is RetrofitResult.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }.exhaustive
}


fun <T> RetrofitStatePagedList<T>.handle(
        loading: () -> Unit,
        noData: () -> Unit,
        emptyData: () -> Unit,
        cantLoadMore: () -> Unit,
        calError: (message: String, throwable: Throwable, exception: Exception?) -> Unit = { _, _, _ -> },
        apiError: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
        success: T.() -> Unit) {

    when (this) {
        is RetrofitStatePagedList.Success -> {
            success.invoke(value)
        }
        RetrofitStatePagedList.Loading -> {
            loading()
        }
        RetrofitStatePagedList.NoData -> {
            noData()

        }
        RetrofitStatePagedList.EmptyData -> {
            emptyData()
        }
        RetrofitStatePagedList.CantLoadMore -> {
            cantLoadMore()
        }
        is RetrofitStatePagedList.Error -> {
            calError(message, throwable, exception)
        }
        is RetrofitStatePagedList.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }.exhaustive
}

const val multiPartContentType = "multipart/form-data"

fun HashMap<String, RequestBody>.addImagesToRetrofit(pathList: List<String>) {
    if (pathList.isNotEmpty()) {
        pathList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = File(s).toRequestBody(MediaType.parse(multiPartContentType))
        }
    }
}

fun HashMap<String, RequestBody>.addImageToRetrofit(path :String?) {
    if (path.isNotNullOrEmpty()) {
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")
            this[key] = File(path).toRequestBody(MediaType.parse(multiPartContentType))
    }
}


fun HashMap<String, RequestBody>.addImageToRetrofit(image: ByteArray?) {
    if (image != null) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")
        this[key] = image.toRequestBody(MediaType.parse(multiPartContentType))
    }
}

fun HashMap<String, RequestBody>.addImageToRetrofit(image: ByteString?) {
    if (image != null) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")
        this[key] = image.toRequestBody(MediaType.parse(multiPartContentType))
    }
}

fun HashMap<String, RequestBody>.addImageBytesToRetrofit(byteList: List<ByteArray>) {
    if (byteList.isNotEmpty()) {
        byteList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = s.toRequestBody(MediaType.parse(multiPartContentType))
        }
    }
}

fun HashMap<String, RequestBody>.addImageByteStringsToRetrofit(byteList: List<ByteString>) {
    if (byteList.isNotEmpty()) {
        byteList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = s.toRequestBody(MediaType.parse(multiPartContentType))
        }
    }
}


val generateRetrofitImageKeyName get() = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")



fun progressDSL(
        onProgressStarted: () -> Unit = {},
        onProgressFinished: () -> Unit = {},
        onProgressChanged: (percent: Int) -> Unit = { _ -> }
): OnAttachmentDownloadListener {
    return object : OnAttachmentDownloadListener {
        override fun onAttachmentDownloadedStarted() {
            onProgressStarted()
        }

        override fun onAttachmentDownloadedFinished() {
            onProgressFinished()
        }

        override fun onAttachmentDownloadUpdate(percent: Int) {
            onProgressChanged(percent)
        }

    }
}

/**
 *
 * val hashMap: HashMap<String, RequestBody> = HashMap()

if (pathList.isNotEmpty()){
hashMap.addImages(pathList.map {
it.pathToImage
})
}
 */
/**
 *
@Multipart
@POST()
fun postNewWaterMeterMeasurementWithImages(@Header("Authorization") token: String,
@PartMap images: Map<String,@JvmSuppressWildcards RequestBody>): Single<Response<ResponseBody>>

 *
 */