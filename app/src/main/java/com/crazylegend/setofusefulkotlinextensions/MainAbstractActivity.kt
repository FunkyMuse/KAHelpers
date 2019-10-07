package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.crazylegend.kotlinextensions.handlers.ViewState
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import kotlinx.android.synthetic.main.activity_main.*

class MainAbstractActivity : AppCompatActivity(R.layout.activity_main) {

    private val testAVM by lazy {
        compatProvider<TestAVM>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testAVM.posts.observe(this, Observer {

        })

        testAVM.viewStateResult?.observe(this, Observer {
            debug("STATE $it")
            it.handle({ //initial state
                isVisible, text ->
            }, {//loading
                isVisible, text ->
            }, { //success
                isVisible, text ->
            }, {//failure
                isVisible, text ->

            })
            when (it) {
                is ViewState.InitialState -> {
                    loading.isVisible = it.isVisible
                    text.setPrecomputedText(it.text)
                }
                is ViewState.Loading -> {
                    loading.isVisible = it.isVisible
                    text.setPrecomputedText(it.text)
                }
                is ViewState.Success -> {
                    loading.isVisible = it.isVisible
                    text.setPrecomputedText(it.text)
                }
                is ViewState.Failure -> {
                    loading.isVisible = it.isVisible
                    text.setPrecomputedText(it.text)
                }
            }
        })
    }
}

private fun ViewState.handle(initialState: (isVisible: Boolean, text: String) -> Unit,
                             loading: (isVisible: Boolean, text: String) -> Unit,
                             success: (isVisible: Boolean, text: String) -> Unit,
                             failure: (isVisible: Boolean, text: String) -> Unit) {
    when (this) {
        is ViewState.InitialState -> {
            initialState.invoke(isVisible, text)
        }
        is ViewState.Loading -> {
            loading.invoke(isVisible, text)

        }
        is ViewState.Success -> {
            success.invoke(isVisible, text)
        }
        is ViewState.Failure -> {
            failure.invoke(isVisible, text)
        }
    }
}


