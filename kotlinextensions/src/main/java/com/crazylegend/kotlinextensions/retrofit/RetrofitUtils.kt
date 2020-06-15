package com.crazylegend.kotlinextensions.retrofit

import android.annotation.SuppressLint
import android.content.Context
import com.crazylegend.kotlinextensions.color.randomColor
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.retrofit.progressInterceptor.OnAttachmentDownloadListener
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.ByteString
import java.io.File
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.random.Random


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */



const val multiPartContentType = "multipart/form-data"

fun HashMap<String, RequestBody>.addImagesToRetrofit(pathList: List<String>) {
    if (pathList.isNotEmpty()) {
        pathList.forEachIndexed { index, s ->
            val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_" + "${index + 1}")
            this[key] = File(s).asRequestBody(multiPartContentType.toMediaType())
        }
    }
}

fun HashMap<String, RequestBody>.addImageToRetrofit(pathToFile: String?, photoIndexNumber: Int = randomColor) {
    if (pathToFile.isNotNullOrEmpty()) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$photoIndexNumber")
        this[key] = File(pathToFile.toString()).asRequestBody(multiPartContentType.toMediaType())
    }
}


fun HashMap<String, RequestBody>.addImageToRetrofit(image: ByteArray?, photoIndexNumber: Int = randomColor) {
    if (image != null) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$photoIndexNumber")
        this[key] = image.toRequestBody(multiPartContentType.toMediaType())
    }
}

fun HashMap<String, RequestBody>.addImageToRetrofit(image: ByteString?, photoIndexNumber: Int = randomColor) {
    if (image != null) {
        val key = String.format("%1\$s\"; filename=\"%1\$s", "photo_$photoIndexNumber")
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

fun generateRetrofitImageKeyName(photoName: String = "photo_${Random.nextInt(0, Int.MAX_VALUE)}") = String.format(
        "%1\$s\"; filename=\"%1\$s",
        photoName
)


fun generateRetrofitImageKeyName(photoIndexNumber: Int = Random.nextInt(0, Int.MAX_VALUE)) = String.format(
        "%1\$s\"; filename=\"%1\$s",
        "photo_${photoIndexNumber}"
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


/**
 * Sets the retrofit to accept all untrusted/unsafe communications, USE WITH CAUTION!
 * @receiver OkHttpClient.Builder
 * @throws CertificateException
 */
fun OkHttpClient.Builder.setUnSafeOkHttpClient() {
    try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
        )
        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}