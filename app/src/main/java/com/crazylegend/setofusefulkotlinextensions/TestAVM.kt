package com.crazylegend.setofusefulkotlinextensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.crazylegend.kotlinextensions.coroutines.makeApiCallLiveDataAsync
import com.crazylegend.kotlinextensions.retrofit.RetrofitClient
import retrofit2.create


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
        RetrofitClient.moshiInstanceCoroutines(application, TestApi.API, true)?.create<TestApi>()
    }

}



