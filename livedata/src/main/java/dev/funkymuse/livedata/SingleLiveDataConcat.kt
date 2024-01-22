package dev.funkymuse.livedata

import androidx.lifecycle.MediatorLiveData



class SingleLiveDataConcat<T>(liveDataList: List<SingleLiveData<T>>) : MediatorLiveData<T>() {
    constructor(vararg liveData: SingleLiveData<T>) : this(liveData.toList())

    private val emittedValues = mutableListOf<T?>()
    private val hasEmittedValues = mutableListOf<Boolean>()
    private var lastEmittedLiveDataIndex = -1

    init {
        (liveDataList.indices).forEach { index ->
            emittedValues.add(null)
            hasEmittedValues.add(false)
            addSource(liveDataList[index]) {
                emittedValues[index] = it
                hasEmittedValues[index] = true
                removeSource(this)
                checkEmit()
            }
        }
    }

    /**
     * Emits the item that are in the `queue`
     */
    private fun checkEmit() {
        synchronized(hasEmittedValues) {
            while (lastEmittedLiveDataIndex < emittedValues.size - 1 && hasEmittedValues[lastEmittedLiveDataIndex + 1]) {
                value = emittedValues[lastEmittedLiveDataIndex + 1]
                lastEmittedLiveDataIndex += 1
            }
        }
    }
}