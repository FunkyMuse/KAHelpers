package com.crazylegend.retrofit.retrofitResult

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by crazy on 1/14/21 to long live and prosper !
 */


suspend fun <T> RetrofitResult<T>.asDatabaseBoundResource(
        provideDatabaseCall: Boolean = true,
        saveToDatabase: suspend (T) -> Unit = {},
        loadFromDatabase: suspend () -> T,
): RetrofitResult<T> = when (this) {
    is RetrofitResult.Success -> {
        saveToDatabase(value)
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    RetrofitResult.EmptyData -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    is RetrofitResult.Error -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    is RetrofitResult.ApiError -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    is RetrofitResult.Loading -> this
}

suspend fun <T> RetrofitResult<T>.asDatabaseBoundResource(
        provideDatabaseCall: Boolean = true,
        loadFromDatabase: suspend () -> T,
): RetrofitResult<T> = when (this) {
    is RetrofitResult.Success -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    RetrofitResult.EmptyData -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    is RetrofitResult.Error -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    is RetrofitResult.ApiError -> {
        checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, loadFromDatabase)
    }
    is RetrofitResult.Loading -> this
}


suspend fun <T> MutableStateFlow<RetrofitResult<T>>.asNetworkBoundResource(
        provideDatabaseCall: Boolean = true,
        saveToDatabase: suspend (T) -> Unit = {},
        shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean = { true },
        loadFromDatabase: suspend () -> T,
        loadFromNetwork: suspend () -> RetrofitResult<T>
) {

    loadFromNetwork().handle(
            loading = {
                value = RetrofitResult.Loading
            },
            emptyData = {
                stateFlowBoundedAsResourceAndDate(provideDatabaseCall, shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, saveToDatabase, loadFromDatabase)
            },
            callError = {
                stateFlowBoundedAsResourceAndDate(provideDatabaseCall, shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, saveToDatabase, loadFromDatabase)
            },
            apiError = { _, _ ->
                stateFlowBoundedAsResourceAndDate(provideDatabaseCall, shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, saveToDatabase, loadFromDatabase)
            },
            success = {
                saveToDatabase(this)
                stateFlowBoundedAsResourceAndDate(provideDatabaseCall, shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, loadFromDatabase)
            }
    )
}

private suspend fun <T> RetrofitResult<T>.checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(
        provideDatabaseCall: Boolean,
        loadFromDatabase: suspend () -> T): RetrofitResult<T> =
        if (provideDatabaseCall) {
            RetrofitResult.Success(loadFromDatabase())
        } else {
            this
        }


private suspend fun <T> MutableStateFlow<RetrofitResult<T>>.stateFlowBoundedAsResourceAndDate(
        provideDatabaseCall: Boolean = true,
        shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean,
        databaseValue: T,
        loadFromNetwork: suspend () -> RetrofitResult<T>,
        saveToDatabase: suspend (T) -> Unit,
        loadFromDatabase: suspend () -> T) {
    if (shouldLoadFromNetworkOnDatabaseCondition(databaseValue)) {
        loadFromNetwork().asDatabaseBoundResource(provideDatabaseCall, saveToDatabase, loadFromDatabase)
    } else {
        value = RetrofitResult.Success(databaseValue)
    }
}

private suspend fun <T> MutableStateFlow<RetrofitResult<T>>.stateFlowBoundedAsResourceAndDate(
        provideDatabaseCall: Boolean = true,
        shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean,
        databaseValue: T,
        loadFromNetwork: suspend () -> RetrofitResult<T>,
        loadFromDatabase: suspend () -> T) {
    if (shouldLoadFromNetworkOnDatabaseCondition(databaseValue)) {
        loadFromNetwork().asDatabaseBoundResource(provideDatabaseCall, loadFromDatabase)
    } else {
        value = RetrofitResult.Success(databaseValue)
    }
}