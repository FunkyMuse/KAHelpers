package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.crazylegend.kotlinextensions.coroutines.makeApiCallAsync
import com.crazylegend.kotlinextensions.coroutines.makeApiCallLiveData
import com.crazylegend.kotlinextensions.coroutines.makeApiCallLiveDataAsync
import com.crazylegend.kotlinextensions.coroutines.makeApiCallLiveDataListAsync
import com.crazylegend.kotlinextensions.handlers.ViewState
import com.crazylegend.kotlinextensions.handlers.hookViewStateResult
import com.crazylegend.kotlinextensions.livedata.liveDataOf
import com.crazylegend.kotlinextensions.retrofit.*


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class TestAVM(application: Application) : AndroidViewModel(application) {

    val posts = makeApiCallLiveDataAsync {
        retrofit?.getPosts()
    }

    private val retrofit by lazy {
        RetrofitClient.moshiInstanceCoroutines(application, TestApi.API, true).create<TestApi>()
    }

}



