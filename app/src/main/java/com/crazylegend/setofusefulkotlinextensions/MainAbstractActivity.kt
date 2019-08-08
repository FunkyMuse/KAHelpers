package com.crazylegend.setofusefulkotlinextensions


import android.content.Intent
import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.storage.createFile
import com.crazylegend.kotlinextensions.storage.getDocumentNameAndSizeForUri
import com.crazylegend.kotlinextensions.storage.openDirectory
import kotlinx.android.synthetic.main.activity_main.*


class MainAbstractActivity : BaseAbstractActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_main

    private val RC_OPEN_DOCUMENT = 131

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        img.setOnClickListener {
           createFile("image/*", "ASDFASDF", RC_OPEN_DOCUMENT)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        debug("URIIII ${data?.data.toString()}")
        debug("URIIII ${data?.dataString.toString()}")
        debug(data?.data?.let {

            getDocumentNameAndSizeForUri(it) { size, name ->
                debug("FILE->SIZE: ${size.toString()}")
                debug("FILE->NAME: ${name.toString()}")
            }


        })
    }

}
