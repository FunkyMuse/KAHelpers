package com.crazylegend.database.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.database.DBResult
import com.crazylegend.database.databaseError
import com.crazylegend.database.databaseSubscribe
import com.crazylegend.database.databaseSubscribeList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Created by crazy on 11/10/20 to long live and prosper !
 */

inline fun <T : DBResult<T>> ViewModel.makeDBCallStateFlow(stateFlow: MutableStateFlow<DBResult<T>>, crossinline dbCall: suspend () -> T) {
    viewModelScope.launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribe(task.await())
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}

inline fun <T : DBResult<T>> ViewModel.makeDBCallListStateFlow(stateFlow: MutableStateFlow<DBResult<T>>,
                                                               includeEmptyData: Boolean = false,
                                                               crossinline dbCall: suspend () -> T) {
    viewModelScope.launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}

inline fun <T : DBResult<T>> CoroutineScope.makeDBCallStateFlow(stateFlow: MutableStateFlow<DBResult<T>>, crossinline dbCall: suspend () -> T) {

    launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying

            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribe(task.await())
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}

inline fun <T : DBResult<T>> CoroutineScope.makeDBCallListStateFlow(stateFlow: MutableStateFlow<DBResult<T>>,
                                                                    includeEmptyData: Boolean = false,
                                                                    crossinline dbCall: suspend () -> T) {
    launch {
        supervisorScope {
            stateFlow.value = DBResult.Querying
            try {
                val task = async(ioDispatcher) {
                    dbCall()
                }
                stateFlow.value = databaseSubscribeList(task.await(), includeEmptyData)
            } catch (t: Throwable) {
                stateFlow.value = databaseError(t)
            }
        }
    }
}
