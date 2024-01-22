package dev.funkymuse.retrofit.progressInterceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException



class OnProgressDownloadInterceptor(private val progressListenerDownload: OnAttachmentDownloadListener?) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (progressListenerDownload == null) return chain.proceed(chain.request())

        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
                .body(originalResponse.body?.let { ProgressResponseBody(it, progressListenerDownload) })
                .build()
    }
}