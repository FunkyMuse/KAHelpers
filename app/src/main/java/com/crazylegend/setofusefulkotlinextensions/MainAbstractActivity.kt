package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import kotlinx.android.synthetic.main.activity_main.*

class MainAbstractActivity : AppCompatActivity(R.layout.activity_main) {

    private val testAVM by lazy {
        compatProvider<TestAVM>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        testAVM.posts?.observe(this, Observer {
            when (it) {
                is RetrofitResult.Success -> {
                    debug(it.toString())
                }
                RetrofitResult.Loading -> {
                    debug(it.toString())
                }
                RetrofitResult.EmptyData -> {
                    debug(it.toString())
                }
                is RetrofitResult.Error -> {
                    debug(it.toString())
                }
                is RetrofitResult.ApiError -> {
                    debug(it.toString())
                }
            }.exhaustive
        })
    }

    private fun loadingSetup(visible: Boolean) {
        loading.isVisible = visible
    }
}


