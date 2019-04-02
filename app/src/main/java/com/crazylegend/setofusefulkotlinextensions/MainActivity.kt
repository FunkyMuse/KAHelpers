package com.crazylegend.setofusefulkotlinextensions





import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseActivity


class MainActivity : BaseActivity() {


    override fun getLayoutResourceId(): Int {
       return R.layout.activity_main
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



}
