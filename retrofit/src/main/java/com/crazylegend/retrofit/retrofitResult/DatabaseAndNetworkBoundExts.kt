package com.crazylegend.retrofit.retrofitResult

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by crazy on 1/14/21 to long live and prosper !
 */


suspend inline fun <T> RetrofitResult<T>.asDatabaseBoundResource(
        provideDatabaseCall: Boolean = true,
        noinline saveToDatabase: suspend (T) -> Unit = {},
        noinline loadFromDatabase: suspend () -> T,
): RetrofitResult<T> {
    return when (this) {
        is RetrofitResult.Success -> {
            saveToDatabase(value)
            checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, this, loadFromDatabase)
        }
        RetrofitResult.EmptyData -> {
            checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, this, loadFromDatabase)
        }
        is RetrofitResult.Error -> {
            checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, this, loadFromDatabase)
        }
        is RetrofitResult.ApiError -> {
            checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(provideDatabaseCall, this, loadFromDatabase)
        }
        is RetrofitResult.Loading -> this

    }
}

suspend inline fun <T> MutableStateFlow<RetrofitResult<T>>.asNetworkBoundResource(
        provideDatabaseCall: Boolean = true,
        noinline saveToDatabase: suspend (T) -> Unit = {},
        noinline shouldLoadFromNetworkOnDatabaseCondition: (T) -> Boolean = { true },
        noinline loadFromDatabase: suspend () -> T,
        noinline loadFromNetwork: suspend () -> RetrofitResult<T>
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
                stateFlowBoundedAsResourceAndDate(provideDatabaseCall, shouldLoadFromNetworkOnDatabaseCondition, loadFromDatabase(), loadFromNetwork, saveToDatabase, loadFromDatabase)
            }
    )
}

suspend inline fun <T> checkWhetherToProvideDatabaseCallOrPropagateCurrentResult(
        provideDatabaseCall: Boolean,
        currentState: RetrofitResult<T>,
        noinline loadFromDatabase: suspend () -> T): RetrofitResult<T> =
        if (provideDatabaseCall) {
            RetrofitResult.Success(loadFromDatabase())
        } else {
            currentState
        }


suspend fun <T> MutableStateFlow<RetrofitResult<T>>.stateFlowBoundedAsResourceAndDate(
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