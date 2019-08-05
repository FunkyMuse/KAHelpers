package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import android.os.TestLooperManager
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.context.showTimePicker
import com.crazylegend.kotlinextensions.dsls.broadcastReceiver
import com.crazylegend.kotlinextensions.livedata.asLiveData
import com.crazylegend.kotlinextensions.livedata.observe
import com.crazylegend.kotlinextensions.services.startForegroundService


class MainAbstractActivity : BaseAbstractActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

}
