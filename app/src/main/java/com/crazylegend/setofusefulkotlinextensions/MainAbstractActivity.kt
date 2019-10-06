package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.log.debug

class MainAbstractActivity : AppCompatActivity(R.layout.activity_main) {

    private val testAVM by lazy {
        compatProvider<TestAVM>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testAVM.posts.observe(this, Observer {

        })

        testAVM.viewStateResult?.observe(this, Observer {
            debug("STATE 44 ${it.toString()}")
        })
    }
}


