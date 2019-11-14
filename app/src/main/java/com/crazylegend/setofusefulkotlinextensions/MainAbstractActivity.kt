package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.delegates.activityVM
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.setofusefulkotlinextensions.test.TestAdapter
import com.crazylegend.setofusefulkotlinextensions.test.TestModel
import kotlinx.android.synthetic.main.activity_main.*

class MainAbstractActivity : AppCompatActivity(R.layout.activity_main) {

    private val testAVM by activityVM<TestAVM>()
    private val adapter by lazy {
        TestAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recycler.setHasFixedSize(false)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        testAVM.posts?.observe(this, Observer {
            when (it) {
                is RetrofitResult.Success -> {
                    debug(it.toString())
                    adapter.submitList(it.value.map {
                        TestModel(it.title)
                    })
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


