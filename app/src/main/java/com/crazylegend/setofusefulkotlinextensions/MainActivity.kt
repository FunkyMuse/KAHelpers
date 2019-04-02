package com.crazylegend.setofusefulkotlinextensions





import android.app.Notification
import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseActivity
import com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography.EncriptionUtil
import com.crazylegend.kotlinextensions.log.debug


class MainActivity : BaseActivity() {


    override fun getLayoutResourceId(): Int {
       return R.layout.activity_main
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       EncriptionUtil.generateKey(this)
        val textEncrypted = EncriptionUtil.encrypt("asdfasdfasdf")
        val textDecrypted = EncriptionUtil.decrypt(textEncrypted)


        textEncrypted.debug("Encrypting $textEncrypted")
        textDecrypted.debug("Decrypting $textDecrypted")


    }




}
