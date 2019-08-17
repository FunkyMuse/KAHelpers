package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity

class MainAbstractActivity : BaseAbstractActivity() {


    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}
