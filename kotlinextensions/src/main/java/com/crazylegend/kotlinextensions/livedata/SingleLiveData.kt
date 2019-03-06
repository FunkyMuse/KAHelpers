package com.crazylegend.kotlinextensions.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */
class SingleLiveData<T>(liveData:LiveData<T>) : MediatorLiveData<T>(){
    private var didSetValue = false
    private val mediatorObserver = Observer<T> {
        synchronized(didSetValue){
            if(!didSetValue){
                didSetValue=true
                this@SingleLiveData.value = it
            }
        }
    }
    init {
        if(liveData.value!=null){
            didSetValue=true
            this.value = liveData.value
        }else {
            addSource(liveData, mediatorObserver)
        }
    }
}