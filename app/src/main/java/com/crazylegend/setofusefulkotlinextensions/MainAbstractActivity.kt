package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.generators.createRandomImageUrl
import com.crazylegend.kotlinextensions.log.debug


class MainAbstractActivity : BaseAbstractActivity() {


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        debug(
            createRandomImageUrl()
        )
    }


}
