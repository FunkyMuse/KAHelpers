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
import retrofit2.Response


/**
 * Created by hristijan on 5/27/19 to long live and prosper !
 */


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
    retrofitResult.loading()
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
    retrofitResult.loading()
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
    dbResult.querying()
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
    dbResult.querying()
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
    retrofitResult.loading()
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
    retrofitResult.loading()
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
    dbResult.querying()
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
    dbResult.querying()
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
        onCallExecuted : () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            onCallExecuted()
        }
    }
}

fun AndroidViewModel.makeDBCall(
        onCallExecuted : () -> Unit = {},
        onErrorAction: (throwable:Throwable)-> Unit = {_->},
        dbCall: suspend () -> Unit): Job {
    return viewModelIOCoroutine {
        try {
            dbCall()
        } catch (t: Throwable) {
            onErrorAction(t)
        }finally {
            onCallExecuted()
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