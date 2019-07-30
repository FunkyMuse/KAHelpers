package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.livedata.asLiveData
import com.crazylegend.kotlinextensions.livedata.observe


class MainAbstractActivity : BaseAbstractActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ld = Long.asLiveData()

       observe(ld){

       }

    }

}
