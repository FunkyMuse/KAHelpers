package com.crazylegend.kotlinextensions.coroutines

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.databaseResult.*
import com.crazylegend.kotlinextensions.retrofit.retrofitResult.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

fun <T> CoroutineScope.makeApiCallAsync(
        response: Response<T>?,
        retrofitResult: MutableLiveData<RetrofitResult<T>>
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    response
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeApiCallListAsync(
        response: Response<T>?,
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    response
                }
                retrofitResult.subscribeListPost(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeDBCallAsync(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()

            try {
                val task = async(ioDispatcher) {
                    queryModel
                }
                dbResult.subscribe(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCallListAsync(
        queryModel: T?,
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true
): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()

            try {
                val task = async(ioDispatcher) {
                    queryModel
                }
                dbResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


fun <T> AndroidViewModel.makeApiCallAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> AndroidViewModel.makeApiCallListAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> AndroidViewModel.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


fun <T> AndroidViewModel.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> T,
        onCalled: (model: T) -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                onCalled(task.await())
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun <T> CoroutineScope.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> T,
        onCalled: (model: T) -> Unit): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                onCalled(task.await())
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun <T> AndroidViewModel.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}

fun AndroidViewModel.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun AndroidViewModel.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun <T> AppCompatActivity.makeApiCallAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()

            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }

    }
}

fun <T> AppCompatActivity.makeApiCallListAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()

            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }

    }
}

fun <T> AppCompatActivity.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {

    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await(), includeEmptyData)

            } catch (t: Throwable) {
                t.printStackTrace()
                dbResult.callError(t)
            }
        }
    }
}


fun <T> AppCompatActivity.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribeList(task.await(), includeEmptyData)

            } catch (t: Throwable) {
                t.printStackTrace()
                dbResult.callError(t)
            }
        }
    }
}


fun AppCompatActivity.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onCallExecuted()
            }
        }
    }
}


fun AppCompatActivity.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {

    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun <T> Fragment.makeApiCallAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()

            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }
}


fun <T> Fragment.makeApiCallListAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()

            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }

    }
}


fun <T> Fragment.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await(), includeEmptyData)

            } catch (t: Throwable) {
                t.printStackTrace()
                dbResult.callError(t)
            }
        }
    }
}


fun <T> Fragment.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribeList(task.await(), includeEmptyData)

            } catch (t: Throwable) {
                t.printStackTrace()
                dbResult.callError(t)
            }
        }
    }
}


fun Fragment.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onCallExecuted()
            }
        }
    }
}

fun Fragment.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return lifecycleScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}


//Api call without wrappers
fun <T> AndroidViewModel.makeApiCallAsync(apiCall: suspend () -> Response<T>?,
                                          onError: (throwable: Throwable) -> Unit = { _ -> },
                                          onUnsuccessfulCall: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
                                          onResponse: (response: T?) -> Unit
): Job {

    return viewModelScope.launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                val response = task.await()
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
}


fun <T> CoroutineScope.makeApiCallAsync(apiCall: suspend () -> Response<T>?,
                                        onError: (throwable: Throwable) -> Unit = { _ -> },
                                        onUnsuccessfulCall: (errorBody: ResponseBody?, responseCode: Int) -> Unit = { _, _ -> },
                                        onResponse: (response: T?) -> Unit
): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                val response = task.await()
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
}


fun <T> CoroutineScope.makeApiCallAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        apiCall: suspend () -> Response<T>?): Job {


    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribe(task.await())
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }

        }
    }
}

fun <T> CoroutineScope.makeApiCallListAsync(
        retrofitResult: MutableLiveData<RetrofitResult<T>>,
        includeEmptyData: Boolean = true,
        apiCall: suspend () -> Response<T>?): Job {
    retrofitResult.loadingPost()

    return launch(mainDispatcher) {
        supervisorScope {
            retrofitResult.loading()
            try {
                val task = async(ioDispatcher) {
                    apiCall()
                }
                retrofitResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                retrofitResult.callError(t)
            }
        }
    }

}


fun <T> CoroutineScope.makeDBCallAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = false,
        dbCall: suspend () -> T?): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribe(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


fun <T> CoroutineScope.makeDBCallListAsync(
        dbResult: MutableLiveData<DBResult<T>>,
        includeEmptyData: Boolean = true,
        dbCall: suspend () -> T?): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            dbResult.querying()
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                dbResult.subscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                dbResult.callError(t)
            }
        }
    }
}


fun CoroutineScope.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        dbCall: suspend () -> Unit): Job {

    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                onCallExecuted()
            }
        }

    }
}


fun CoroutineScope.makeDBCallAsync(
        onCallExecuted: () -> Unit = {},
        onErrorAction: (throwable: Throwable) -> Unit = { _ -> },
        dbCall: suspend () -> Unit): Job {
    return launch(mainDispatcher) {
        supervisorScope {
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                task.await()
            } catch (t: Throwable) {
                t.printStackTrace()
                onErrorAction(t)
            } finally {
                onCallExecuted()
            }
        }
    }
}