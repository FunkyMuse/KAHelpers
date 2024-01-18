package com.funkymuse.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData




/**
 * Emits the items that are different from the last item
 */
val <T> LiveData<T>.distinctUntilChanged: LiveData<T>
    get() {
        val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
        var latestValue: T? = null
        mutableLiveData.addSource(this) {
            if (latestValue != it) {
                mutableLiveData.value = it
                latestValue = it
            }
        }
        return mutableLiveData
    }


/**
 * Emits the items that are different from all the values that have been emitted so far
 */
val <T> LiveData<T>.distinct: LiveData<T>
    get() {
        val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
        val dispatchedValues = mutableListOf<T?>()
        mutableLiveData.addSource(this) {
            if (!dispatchedValues.contains(it)) {
                mutableLiveData.value = it
                dispatchedValues.add(it)
            }
        }
        return mutableLiveData
    }

/**
 * Emits only the values that are not null
 */
val <T> LiveData<T>.nonNullValues: NonNullLiveData<T>
    get() {
        return NonNullLiveData(this)
    }


/**
 * Converts a LiveData to a SingleLiveData
 */
val <T> LiveData<T>.toSingleLiveData: SingleLiveData<T> get() = first

/**
 * Converts a LiveData to a MutableLiveData with the initial value set by this LiveData's value
 */
val <T> LiveData<T>.toMutableLiveData: MutableLiveData<T>
    get() {
        val liveData = MutableLiveData<T>()
        liveData.value = this.value
        return liveData
    }

/**
 * Emits at most 1 item and returns a SingleLiveData
 */
val <T> LiveData<T>.first: SingleLiveData<T> get() = SingleLiveData(this)