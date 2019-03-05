package com.crazylegend.kotlinextensions.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */
class NonNullLiveData<T>(liveData:LiveData<T>) : LiveData<T>() {

    private val mediatorLiveData = MediatorLiveData<T>()
    private val mediatorObserver = Observer<T> {
        it?.let{
            this@NonNullLiveData.value = it
        }
    }
    init {
        mediatorLiveData.observeForever(mediatorObserver)
        mediatorLiveData.addSource(liveData, mediatorObserver)
    }
    override fun onInactive() {
        super.onInactive()
        mediatorLiveData.removeObserver(mediatorObserver)
    }

}