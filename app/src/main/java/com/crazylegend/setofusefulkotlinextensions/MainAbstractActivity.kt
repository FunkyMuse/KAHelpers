package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.lifecycle.Observer
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.retrofit.handle

class MainAbstractActivity : BaseAbstractActivity() {


    override fun getLayoutResourceId(): Int = R.layout.activity_main

    private val testAVM by lazy {
        compatProvider<TestAVM>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testAVM.posts.observe(this, Observer {
            it.handle({
                //loading
                debug("LOADING")
            }, {
                //empty data
                debug("EMPTY DATA")
            }, { // call error
                throwable ->
                throwable.printStackTrace()
                debug(throwable.message.toString())
            }, { // api error
                errorBody, responseCode ->
                debug("Error ${errorBody?.string().toString()} response code = $responseCode")

            }, {
                //success handle
                debug("SUCCESS $this")
            })
        })
    }


}
