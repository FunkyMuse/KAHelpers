package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.crazylegend.kotlinextensions.handlers.handle
import com.crazylegend.kotlinextensions.livedata.compatProvider
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
            it.handle({ //initial state
                isVisible, text ->
                loadingSetup(isVisible, text)
            }, {//loading
                isVisible, text ->
                loadingSetup(isVisible, text)
            }, { //success
                isVisible, text ->
                loadingSetup(isVisible, text)
            }, {//failure
                isVisible, text ->
                loadingSetup(isVisible, text)
            })
        })
    }

    private fun loadingSetup(visible: Boolean, text: String) {
        loading.isVisible = visible
        textSet.setPrecomputedText(text)
        textSet.isVisible = visible
    }
}


