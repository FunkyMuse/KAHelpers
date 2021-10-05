package com.crazylegend.retrofit.retrofitResult

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by crazy on 1/14/21 to long live and prosper !
 */

private suspend fun <T> RetrofitResult<T>.asDatabaseBoundResource(
        saveToDatabase: suspend (T) -> Unit = {},
        loadFromDatabase: suspend () -> T,
): RetrofitResult<T> = when (this) {
    is RetrofitResult.Success -> {
        saveToDatabase(value)
        propagateDatabaseResult(loadFromDatabase)
    }
    is RetrofitResult.Error -> propagateDatabaseResult(loadFromDatabase)
    is RetrofitResult.ApiError -> propagateDatabaseResult(loadFromDatabase)
    is RetrofitResult.Loading -> this
    RetrofitResult.Idle -> this
}

suspend fun <T> MutableStateFlow<RetrofitResult<T>>.asDatabaseBoundResource(
        saveToDatabase: suspend (T) -> Unit = {},
        loadFromDatabase: suspend () -> T,
) {
    when (val data = value) {
        is RetrofitResult.Success -> {
            saveToDatabase(data.value)
            propagateDatabaseResult(loadFromDatabase)
        }
        is RetrofitResult.Error -> {
            emit(data)
            propagateDatabaseResult(loadFromDatabase)
        }
        is RetrofitResult.ApiError -> {
            emit(data)
            propagateDatabaseResult(loadFromDatabase)
        }
        is RetrofitResult.Loading -> emit(data)
        is RetrofitResult.Idle -> emit(data)
    }
}


suspend fun <T> MutableStateFlow<RetrofitResult<T>>.asNetworkBoundResource(
        saveToDatabase: suspend (T) -> Unit = {},
        shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean = { true },
        loadFromDatabase: suspend () -> T,
        loadFromNetwork: suspend () -> RetrofitResult<T>
) {

    when (val data = loadFromNetwork()) {
        is RetrofitResult.Success -> {
            saveToDatabase(data.value)
            stateFlowBoundedAsResourceAndData(shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, loadFromDatabase)
        }
        RetrofitResult.Loading -> value = data
        else -> {
            value = data
            stateFlowBoundedAsResourceAndData(shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, saveToDatabase, loadFromDatabase)
        }
    }
}

private suspend fun <T> propagateDatabaseResult(loadFromDatabase: suspend () -> T): RetrofitResult<T> = RetrofitResult.Success(loadFromDatabase())


private suspend fun <T> MutableStateFlow<RetrofitResult<T>>.stateFlowBoundedAsResourceAndData(
        shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean,
        databaseValue: T,
        loadFromNetwork: suspend () -> RetrofitResult<T>,
        saveToDatabase: suspend (T) -> Unit,
        loadFromDatabase: suspend () -> T) {
    if (shouldLoadFromNetworkOnDatabaseCondition(databaseValue)) {
        loadFromNetwork().asDatabaseBoundResource(saveToDatabase, loadFromDatabase)
    } else {
        value = RetrofitResult.Success(databaseValue)
    }
}

private suspend fun <T> MutableStateFlow<RetrofitResult<T>>.stateFlowBoundedAsResourceAndData(
        shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean,
        databaseValue: T,
        loadFromNetwork: suspend () -> RetrofitResult<T>,
        loadFromDatabase: suspend () -> T) {
    if (shouldLoadFromNetworkOnDatabaseCondition(databaseValue)) {
        loadFromNetwork().asDatabaseBoundResource(loadFromDatabase = loadFromDatabase)
    } else {
        value = RetrofitResult.Success(databaseValue)
    }
}