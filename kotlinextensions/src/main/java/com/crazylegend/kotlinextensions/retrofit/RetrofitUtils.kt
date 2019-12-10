package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.color.randomColor
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.retrofit.progressInterceptor.OnAttachmentDownloadListener
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.ByteString
import retrofit2.Response
import java.io.File
import kotlin.random.Random


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */


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
        emptyData: () -> Unit,
        calError: (throwable: Throwable) -> Unit = { _ -> },
        apiError: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
        success: T.() -> Unit
) {

    when (this) {
        is RetrofitResult.Success -> {
            success.invoke(value)
        }
        RetrofitResult.Loading -> {
            loading()
        }
        RetrofitResult.EmptyData -> {
            emptyData()
        }
        is RetrofitResult.Error -> {
            calError(throwable)
        }
        is RetrofitResult.ApiError -> {
            apiError(errorBody, responseCode)
        }
    }.exhaustive
}


const val multiPartContentType = "multipart/form-data"

fun HashMap<String, RequestBody>.addImagesToRetrofit(pathList: List<String>) {
    if (pathList.isNotEmpty()) {
        pathList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = File(s).asRequestBody(multiPartContentType.toMediaType())
        }
    }
}

fun HashMap<String, RequestBody>.addImageToRetrofit(pathToFile: String?) {
    if (pathToFile.isNotNullOrEmpty()) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")
        this[key] = File(pathToFile.toString()).asRequestBody(multiPartContentType.toMediaType())
    }
}


fun HashMap<String, RequestBody>.addImageToRetrofit(image: ByteArray?) {
    if (image != null) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")
        this[key] = image.toRequestBody(multiPartContentType.toMediaType())
    }
}

fun HashMap<String, RequestBody>.addImageToRetrofit(image: ByteString?) {
    if (image != null) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$randomColor")
        this[key] = image.toRequestBody(multiPartContentType.toMediaType())
    }
}

fun HashMap<String, RequestBody>.addImageBytesToRetrofit(byteList: List<ByteArray>) {
    if (byteList.isNotEmpty()) {
        byteList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = s.toRequestBody(multiPartContentType.toMediaType())
        }
    }
}

fun HashMap<String, RequestBody>.addImageByteStringsToRetrofit(byteList: List<ByteString>) {
    if (byteList.isNotEmpty()) {
        byteList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = s.toRequestBody(multiPartContentType.toMediaType())
        }
    }
}


val generateRetrofitImageKeyName
    get() = String.format(
            "%1\$s\"; filename=\"%1\$s",
            "photo_${Random.nextInt(0, Int.MAX_VALUE)}"
    )

fun Double?.toRequestBodyForm(): RequestBody {
    return this.toString().toRequestBody(MultipartBody.FORM)
}

fun String?.toRequestBodyForm(): RequestBody {
    return this.toString().toRequestBody(MultipartBody.FORM)
}

fun Int?.toRequestBodyForm(): RequestBody {
    return this.toString().toRequestBody(MultipartBody.FORM)
}

fun Float?.toRequestBodyForm(): RequestBody {
    return this.toString().toRequestBody(MultipartBody.FORM)
}

fun Any?.toRequestBodyForm(): RequestBody {
    return toString().toRequestBodyForm()
}

fun <T> MutableLiveData<RetrofitResult<T>>.loading() {
    value = RetrofitResult.Loading
}

fun <T> MutableLiveData<RetrofitResult<T>>.emptyData() {
    value = RetrofitResult.EmptyData
}


fun <T> MutableLiveData<RetrofitResult<T>>.loadingPost() {
    postValue(RetrofitResult.Loading)
}

fun <T> MutableLiveData<RetrofitResult<T>>.emptyDataPost() {
    postValue(RetrofitResult.EmptyData)
}


inline fun <reified T> isGenericInstanceOf(obj: Any): Boolean = obj is T


fun <T> MutableLiveData<RetrofitResult<T>>.subscribe(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        value = RetrofitResult.EmptyData
                    } else {
                        value = RetrofitResult.Success(this)
                    }
                } else {
                    value = RetrofitResult.Success(this)
                }
            }
        } else {
            value = RetrofitResult.ApiError(serverResponse.code(), serverResponse.errorBody())
        }
    }
}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribePost(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        postValue(RetrofitResult.EmptyData)
                    } else {
                        postValue(RetrofitResult.Success(this))
                    }
                } else {
                    postValue(RetrofitResult.Success(this))
                }
            }
        } else {
            postValue(RetrofitResult.ApiError(serverResponse.code(), serverResponse.errorBody()))
        }
    }
}


fun <T> MutableLiveData<RetrofitResult<T>>.subscribeList(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        value = RetrofitResult.EmptyData
                    } else {
                        if (this is List<*>) {
                            val list = this as List<*>
                            if (list.isNullOrEmpty()) {
                                value = RetrofitResult.EmptyData
                            } else {
                                value = RetrofitResult.Success(this)
                            }
                        } else {
                            value = RetrofitResult.Success(this)
                        }
                    }
                } else {
                    value = RetrofitResult.Success(this)
                }
            }
        } else {
            value = RetrofitResult.ApiError(serverResponse.code(), serverResponse.errorBody())
        }
    }

}

fun <T> MutableLiveData<RetrofitResult<T>>.subscribeListPost(response: Response<T>?, includeEmptyData: Boolean = false) {
    response?.let { serverResponse ->
        if (serverResponse.isSuccessful) {
            serverResponse.body()?.apply {
                if (includeEmptyData) {
                    if (this == null) {
                        postValue(RetrofitResult.EmptyData)
                    } else {
                        if (this is List<*>) {
                            val list = this as List<*>
                            if (list.isNullOrEmpty()) {
                                postValue(RetrofitResult.EmptyData)
                            } else {
                                postValue(RetrofitResult.Success(this))
                            }
                        } else {
                            postValue(RetrofitResult.Success(this))
                        }
                    }
                } else {
                    postValue(RetrofitResult.Success(this))
                }
            }
        } else {
            postValue(RetrofitResult.ApiError(serverResponse.code(), serverResponse.errorBody()))
        }
    }
}

fun <T> MutableLiveData<RetrofitResult<T>>.callError(throwable: Throwable) {
    value = RetrofitResult.Error(throwable)
}

fun <T> MutableLiveData<RetrofitResult<T>>.callErrorPost(throwable: Throwable) {
    postValue(RetrofitResult.Error(throwable))
}

fun <T> MutableLiveData<RetrofitResult<T>>.success(model: T) {
    value = RetrofitResult.Success(model)
}

fun <T> MutableLiveData<RetrofitResult<T>>.successPost(model: T) {
    postValue(RetrofitResult.Success(model))
}

fun <T> MutableLiveData<RetrofitResult<T>>.apiError(code: Int, errorBody: ResponseBody?) {
    value = RetrofitResult.ApiError(code, errorBody)
}


fun <T> MutableLiveData<RetrofitResult<T>>.apiErrorPost(code: Int, errorBody: ResponseBody?) {
    postValue(RetrofitResult.ApiError(code, errorBody))
}

//success

fun <T> MutableLiveData<RetrofitResult<T>>.onSuccess(action: (model: T) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Success -> {
                action(it.value)
            }
            else -> {
            }
        }
    }
}

val <T> MutableLiveData<RetrofitResult<T>>.getSuccess : T?  get() {
   return value?.let {
        when (it) {
            is RetrofitResult.Success -> {
                it.value
            }
            else -> {
                null
            }
        }
    }
}

val <T> LiveData<RetrofitResult<T>>.getSuccess: T?  get() {
    return value?.let {
        when (it) {
            is RetrofitResult.Success -> {
                it.value
            }
            else -> {
                null
            }
        }
    }
}

//Loading


fun <T> MutableLiveData<RetrofitResult<T>>.onLoading(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.Loading -> {
                action()
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onLoading(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.Loading -> {
                action()
            }
            else -> {
            }
        }
    }
}


// Empty data


fun <T> MutableLiveData<RetrofitResult<T>>.onEmptyData(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.EmptyData -> {
                action()
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onEmptyData(action: () -> Unit = { }) {
    value?.let {
        when (it) {
            is RetrofitResult.EmptyData -> {
                action()
            }
            else -> {
            }
        }
    }
}

// on call error on your side


fun <T> MutableLiveData<RetrofitResult<T>>.onCallError(action: (throwable: Throwable) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Error -> {
                action(it.throwable)
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onCallError(action: (throwable: Throwable) -> Unit = { _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.Error -> {
                action(it.throwable)
            }
            else -> {
            }
        }
    }
}

// on api error on server side


fun <T> MutableLiveData<RetrofitResult<T>>.onApiError(action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onApiError(action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> MutableLiveData<RetrofitResult<T>>.onApiError(context: Context, action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                context.errorResponseCode(it.responseCode)
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> LiveData<RetrofitResult<T>>.onApiError(context: Context, action: (responseCode: Int, errorBody: ResponseBody?) -> Unit = { _, _ -> }) {
    value?.let {
        when (it) {
            is RetrofitResult.ApiError -> {
                context.errorResponseCode(it.responseCode)
                action(it.responseCode, it.errorBody)
            }
            else -> {
            }
        }
    }
}

fun <T> RetrofitResult<T>.onLoading(function: () -> Unit = {}) {
    if (this is RetrofitResult.Loading) function()
}

fun <T> RetrofitResult<T>.onEmptyData(function: () -> Unit = {}) {
    if (this is RetrofitResult.EmptyData) function()
}

fun <T> RetrofitResult<T>.onCallError(function: (throwable: Throwable) -> Unit = { _ -> }) {
    if (this is RetrofitResult.Error) {
        function(throwable)
    }
}

fun <T> RetrofitResult<T>.onApiError(function: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> }) {
    if (this is RetrofitResult.ApiError) {
        function(errorBody, responseCode)
    }
}

fun <T> RetrofitResult<T>.onSuccess(function: (model: T) -> Unit = { _ -> }) {
    if (this is RetrofitResult.Success) {
        function(value)
    }
}


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

fun Context.errorResponseCode(responseCode: Int) {
    when (responseCode) {

        301 -> {
            shortToast("Moved permanently")
        }

        400 -> {
            // bad request
            shortToast("Bad Request")
        }

        401 -> {
            // unauthorized
            shortToast("Unauthorized")
        }

        403 -> {
            shortToast("Forbidden")
        }

        404 -> {
            // not found
            shortToast("Not found")
        }

        405 -> {
            shortToast("Method not allowed")
        }

        406 -> {
            shortToast("Not acceptable")
        }

        407 -> {
            shortToast("Proxy authentication required")
        }

        408 -> {
            // time out
            shortToast("Time out")
        }

        409 -> {
            shortToast("Conflict error")
        }

        410 -> {
            shortToast("Request permanently deleted")
        }

        413 -> {
            shortToast("Request too large")
        }

        422 -> {
            // account exists
            shortToast("Account with that email already exists")
        }

        425 -> {
            shortToast("Server is busy")
        }

        429 -> {
            shortToast("Too many requests, slow down")
        }

        500 -> {
            // internal server error
            shortToast("Server error")
        }

        501 -> {
            shortToast("Not implemented")
        }

        502 -> {
            // bad gateway
            shortToast("Bad gateway")
        }
        504 -> {
            // gateway timeout
            shortToast("Gateway timeout")
        }

        511 -> {
            shortToast("Authentication required")
        }

        else -> {
            shortToast("Something went wrong, try again")
        }
    }
}


/**
 *
 * Use as
 * OkHttpClient.Builder().cache(cache)
 *
 * @receiver Context needed for obtaining the cachedir
 * @param cacheDirName String initial is "http-cache"
 * @param cacheSize Int initial is 10 * 1024 * 1024 // 10 MB
 * @return Cache
 */
fun Context.retrofitCache(cacheDirName: String = "http-cache",
                          cacheSize: Int = 10 * 1024 * 1024): Cache {
    val httpCacheDirectory = File(cacheDir, cacheDirName)
    return Cache(httpCacheDirectory, cacheSize.toLong())
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
