package com.crazylegend.setofusefulkotlinextensions


import android.Manifest
import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.context.showConfirmationDialog
import com.crazylegend.kotlinextensions.coroutines.mainCoroutine
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.permissionHandlers.PermissionResult
import com.crazylegend.kotlinextensions.permissionHandlers.coroutines.PermissionCouroutineManager


class MainAbstractActivity : BaseAbstractActivity() {



    val REQUEST_ID = 123


    override fun getLayoutResourceId(): Int = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }


}
