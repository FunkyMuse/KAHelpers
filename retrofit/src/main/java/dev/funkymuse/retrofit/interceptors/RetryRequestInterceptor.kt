package dev.funkymuse.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit



class RetryRequestInterceptor(
    private val maxTries: Int = 3,
    private val delayBetweenRetry: Long = TimeUnit.SECONDS.toMillis(3)
) : Interceptor {

    private var tryCount = 0

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        var response: Response = chain.proceed(request)


        while (!response.isSuccessful && tryCount < maxTries) {
            Thread.sleep(delayBetweenRetry)
            response.close()
            response = chain.proceed(request)
            tryCount++
        }

        return response
    }
}