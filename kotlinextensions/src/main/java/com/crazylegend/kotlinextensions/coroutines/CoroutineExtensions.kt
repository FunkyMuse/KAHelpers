package com.crazylegend.kotlinextensions.coroutines

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.database.*
import com.crazylegend.kotlinextensions.retrofit.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


/**
 * Created by hristijan on 5/27/19 to long live and prosper !
 */

suspend inline fun <T, R> T.onMain(crossinline block: (T) -> R): R {
    return withContext(Dispatchers.Main) { this@onMain.let(block) }
}

suspend inline fun <T> onMain(crossinline block: CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Main) { block.invoke(this@withContext) }
}

suspend inline fun <T, R> T.onDefault(crossinline block: (T) -> R): R {
    return withContext(Dispatchers.Default) { this@onDefault.let(block) }
}

suspend inline fun <T> onDefault(crossinline block: CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Default) { block.invoke(this@withContext) }
}

suspend inline fun <T, R> T.onIO(crossinline block: (T) -> R): R {
    return withContext(Dispatchers.IO) { this@onIO.let(block) }
}

suspend inline fun <T> onIO(crossinline block: CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO) { block.invoke(this@withContext) }
}

val mainDispatcher = Dispatchers.Main
val defaultDispatcher = Dispatchers.Default
val unconfinedDispatcher = Dispatchers.Unconfined
val ioDispatcher = Dispatchers.IO


fun <T> ioCoroutineGlobal(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        block()
    }
}

fun <T> mainCoroutineGlobal(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Main) {
        block()
    }
}

fun <T> defaultCoroutineGlobal(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Default) {
        block()
    }
}

fun <T> unconfinedCoroutineGlobal(block: suspend () -> T): Job {
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
    retrofitResult.loadingPost()
    return launch(Dispatchers.IO) {
        try {
            retrofitResult.subscribePost(response, includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }

}

fun <T> CoroutineScope.makeApiCallList(
        response: Response<T>?,
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    retrofitResult.loadingPost()
    return launch(Dispatchers.IO) {
        try {
            retrofitResult.subscribeListPost(response, includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }

}


/**

USAGE:

viewModelScope.launch {
makeDBCall(db?.getSomething(), dbResult)
}

 * @receiver CoroutineScope
 * @param queryModel Response<T>?
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @return Job
 */
fun <T> CoroutineScope.makeDBCall(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false
): Job {
    dbResult.queryingPost()
    return launch(Dispatchers.IO) {
        try {
            dbResult.subscribePost(queryModel, includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


/**

USAGE:

viewModelScope.launch {
makeDBCall(db?.getSomething(), dbResult)
}

 * @receiver CoroutineScope
 * @param queryModel Response<T>?
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @return Job
 */
fun <T> CoroutineScope.makeDBCallList(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    dbResult.queryingPost()
    return launch(Dispatchers.IO) {
        try {
            dbResult.subscribeListPost(queryModel, includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }

}

/**
 * Android View model coroutine extensions
 * Must include the view model androidX for coroutines to provide view model scope
 */


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.apiCall()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> AndroidViewModel.makeApiCall(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = false,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return viewModelIOCoroutine {
        try {
            retrofitResult.subscribePost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }
}


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.makeApiCallList()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> AndroidViewModel.makeApiCallList(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return viewModelIOCoroutine {
        try {
            retrofitResult.subscribeListPost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> AndroidViewModel.makeDBCall(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribePost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

/**
 * Must include empty data
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> AndroidViewModel.makeDBCallList(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */

fun AndroidViewModel.makeDBCall(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            viewModelMainCoroutine {
                onCallExecuted()
            }
        }
    }
}

fun AndroidViewModel.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            viewModelMainCoroutine {
                onCallExecuted()
            }
        }
    }
}


/**
 *
 * @receiver AndroidViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun AndroidViewModel.viewModelIOCoroutine(action: suspend () -> Unit = {}): Job {
    return viewModelScope.launch(ioDispatcher) {
        action()
    }
}


/**
 *
 * @receiver AndroidViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun AndroidViewModel.viewModelMainCoroutine(action: suspend () -> Unit = {}): Job {
    return viewModelScope.launch(mainDispatcher) {
        action()
    }
}


/**
 *
 * @receiver AndroidViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun AndroidViewModel.viewModelDefaultCoroutine(action: suspend () -> Unit = {}): Job {
    return viewModelScope.launch(defaultDispatcher) {
        action()
    }
}

/**
 *
 * @receiver AndroidViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun AndroidViewModel.viewModelUnconfinedCoroutine(action: suspend () -> Unit = {}): Job {
    return viewModelScope.launch(unconfinedDispatcher) {
        action()
    }
}


fun Fragment.ioCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(ioDispatcher) {
        action()
    }
}

fun Fragment.mainCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(mainDispatcher) {
        action()
    }
}

fun Fragment.unconfinedCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(unconfinedDispatcher) {
        action()
    }
}

fun Fragment.defaultCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(defaultDispatcher) {
        action()
    }
}


fun AppCompatActivity.ioCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(ioDispatcher) {
        action()
    }
}

fun AppCompatActivity.mainCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(mainDispatcher) {
        action()
    }
}

fun AppCompatActivity.unconfinedCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(unconfinedDispatcher) {
        action()
    }
}

fun AppCompatActivity.defaultCoroutine(action: suspend () -> Unit = {}): Job {
    return lifecycleScope.launch(defaultDispatcher) {
        action()
    }
}


/**
 * Appcompat activity coroutine extensions
 * Must include the view model androidX for coroutines to provide view model scope
 */


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.apiCall()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> AppCompatActivity.makeApiCall(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = false,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return ioCoroutine {
        try {
            retrofitResult.subscribePost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }
}


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.makeApiCallList()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> AppCompatActivity.makeApiCallList(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return ioCoroutine {
        try {
            retrofitResult.subscribeListPost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> AppCompatActivity.makeDBCall(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            dbResult.subscribePost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

/**
 * Must include empty data
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> AppCompatActivity.makeDBCallList(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */

fun AppCompatActivity.makeDBCall(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return ioCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            mainCoroutine {
                onCallExecuted()
            }
        }
    }
}

fun AppCompatActivity.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return ioCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            mainCoroutine {
                onCallExecuted()
            }
        }
    }
}


/**
 * Appcompat activity coroutine extensions
 * Must include the view model androidX for coroutines to provide view model scope
 */


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.apiCall()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> Fragment.makeApiCall(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = false,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return ioCoroutine {
        try {
            retrofitResult.subscribePost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }
}


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.makeApiCallList()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> Fragment.makeApiCallList(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return ioCoroutine {
        try {
            retrofitResult.subscribeListPost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)
        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> Fragment.makeDBCall(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            dbResult.subscribePost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

/**
 * Must include empty data
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> Fragment.makeDBCallList(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */

fun Fragment.makeDBCall(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return ioCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            mainCoroutine {
                onCallExecuted()
            }
        }
    }
}

fun Fragment.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return ioCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            onErrorAction(t)
        } finally {
            mainCoroutine {
                onCallExecuted()
            }
        }
    }
}


//Api call without wrappers
fun <T> AndroidViewModel.makeApiCall(apiCall: suspend () -> Response<T>?,
                                     onError: (throwable: Throwable) -> Unit = { _ -> },
                                     onUnsuccessfulCall: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
                                     onResponse: (response: T?) -> Unit
): Job {

    return viewModelIOCoroutine {
        try {
            val response = apiCall()
            response?.apply {
                if (isSuccessful) {
                    onResponse(body())
                } else {
                    onUnsuccessfulCall(errorBody(), code())
                }
            }

        } catch (t: Throwable) {
            onError(t)
        }
    }
}


fun <T> CoroutineScope.makeApiCall(apiCall: suspend () -> Response<T>?,
                                   onError: (throwable: Throwable) -> Unit = { _ -> },
                                   onUnsuccessfulCall: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
                                   onResponse: (response: T?) -> Unit
): Job {

    return launch(ioDispatcher) {
        try {
            val response = apiCall()
            response?.apply {
                if (isSuccessful) {
                    onResponse(body())
                } else {
                    onUnsuccessfulCall(errorBody(), code())
                }
            }
        } catch (t: Throwable) {
            onError(t)
        }
    }
}


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.apiCall()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> CoroutineScope.makeApiCall(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = false,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()
    return launch(ioDispatcher) {
        try {
            retrofitResult.subscribePost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)

        }
    }

}


/**
 * USAGE:
makeApiCall(sentResultData) {
retrofitClient?.makeApiCallList()
}
 * @receiver AndroidViewModel
 * @param retrofitResult MutableLiveData<RetrofitResult<T>>
 * @param includeEmptyData Boolean
 * @param apiCall SuspendFunction0<Response<T>?>
 * @return Job
 */
fun <T> CoroutineScope.makeApiCallList(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()

    return launch(ioDispatcher) {
        try {
            retrofitResult.subscribeListPost(apiCall(), includeEmptyData)
        } catch (t: Throwable) {
            retrofitResult.callErrorPost(t)

        }
    }

}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> CoroutineScope.makeDBCall(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribePost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}

/**
 * Must include empty data
 * @receiver AndroidViewModel
 * @param dbResult MutableLiveData<DBResult<T>>
 * @param includeEmptyData Boolean
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */
fun <T> CoroutineScope.makeDBCallList(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {
            dbResult.subscribeListPost(dbCall(), includeEmptyData)
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}

/**
 * USAGE:
makeApiCall(dbResult) {
db?.getDBSomething()
}
 * @receiver AndroidViewModel
 * @param dbCall SuspendFunction0<T?>
 * @return Job
 */

fun CoroutineScope.makeDBCall(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {

    return launch(ioDispatcher) {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}

fun CoroutineScope.makeDBCall(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return launch(ioDispatcher) {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
            launch(mainDispatcher) {
                onErrorAction(t)
            }
        } finally {
            launch(mainDispatcher) {
                onCallExecuted()
            }
        }
    }
}


//flow

fun <T> CoroutineScope.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()

    return launch(ioDispatcher) {
        try {

            val result = dbCall()
            result?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)

        }
    }
}

fun <T> CoroutineScope.makeDBCallFlow(
        queryModel: Flow<T>?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false
): Job {
    dbResult.queryingPost()
    return launch(Dispatchers.IO) {
        try {

            val call = queryModel

            call?.collect {
                dbResult.subscribePost(it, includeEmptyData)
            }

        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

fun <T> CoroutineScope.makeDBCallListFlow(
        queryModel: Flow<T>?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    dbResult.queryingPost()
    return launch(Dispatchers.IO) {
        try {
            val call = queryModel

            call?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }

}


fun <T> AndroidViewModel.makeDBCallFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribePost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> AndroidViewModel.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return viewModelIOCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> AppCompatActivity.makeDBCallFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            val flow = dbCall()
            flow?.collect{
                dbResult.subscribePost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> AppCompatActivity.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}


fun <T> Fragment.makeDBCallFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}



fun <T> Fragment.makeDBCallListFlow(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> Flow<T>?): Job {
    dbResult.queryingPost()
    return ioCoroutine {
        try {
            val flow = dbCall()
            flow?.collect {
                dbResult.subscribeListPost(it, includeEmptyData)
            }
        } catch (t: Throwable) {
            dbResult.callErrorPost(t)
        }
    }
}

