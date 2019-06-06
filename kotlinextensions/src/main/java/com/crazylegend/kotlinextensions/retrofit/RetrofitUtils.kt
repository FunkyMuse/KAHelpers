package com.crazylegend.kotlinextensions.retrofit

import com.crazylegend.kotlinextensions.exhaustive
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.File
import okhttp3.RequestBody.Companion.toRequestBody


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
        calError: (message: String, throwable: Throwable, exception:Exception?) -> Unit = { _, _,_ -> },
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

const val multiPartContentType = "multipart/form-data"

fun HashMap<String,RequestBody>.addImagesToRetrofit(pathList:List<String>){
    if (pathList.isNotEmpty()){
        pathList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index+1}")
            this[key] = File(s).toRequestBody(MediaType.parse(multiPartContentType))
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