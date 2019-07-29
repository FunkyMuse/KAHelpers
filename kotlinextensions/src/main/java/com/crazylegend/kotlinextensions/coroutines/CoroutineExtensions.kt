package com.crazylegend.kotlinextensions.coroutines

import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.retrofit.*
import kotlinx.coroutines.*
import retrofit2.Response


/**
 * Created by hristijan on 5/27/19 to long live and prosper !
 */


fun <T> ioCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        block()
    }
}

fun <T> mainCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Main) {
        block()
    }
}

fun <T> defaultCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Default) {
        block()
    }
}

fun <T> unconfinedCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Unconfined) {
        block()
    }
}

suspend fun <T> withMainContext(block: suspend () -> T): T {
    return withContext(Dispatchers.Main) {
        block()
    }
}

suspend fun <T> withIOContext(block: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        block()
    }
}


suspend fun <T> withDefaultContext(block: suspend () -> T): T {
    return withContext(Dispatchers.Default) {
        block()
    }
}


suspend fun <T> withUnconfinedContext(block: suspend () -> T): T {
    return withContext(Dispatchers.Unconfined) {
        block()
    }
}

/**

USAGE:

viewModelScope.launch {
makeApiCall(client?.getSomething(), retrofitResult)
}

 * @receiver CoroutineScope
 * @param response Response<T>?
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @return Job
 */
fun <T> CoroutineScope.makeApiCall(
        response: Response<T>?,
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = false
): Job {
    retrofitResult.loading()
    return launch(Dispatchers.IO) {
        try {
            response?.apply {
                if (isSuccessful) {

                    if (includeEmptyData) {
                        val model = body()
                        if (model == null) {
                            retrofitResult.emptyDataPost()
                        } else {
                            retrofitResult.successPost(model)
                        }
                    } else {
                        body()?.apply {
                            retrofitResult.successPost(this)
                        }
                    }
                } else {
                    mainCoroutine {
                        retrofitResult.apiErrorPost(code(), errorBody())
                    }
                }
            }

        } catch (t: Throwable) {
            mainCoroutine {
                retrofitResult.callErrorPost(t)
            }
        }
    }

}