package com.crazylegend.setofusefulkotlinextensions





import android.os.Bundle
import com.crazylegend.kotlinextensions.codestyle.BaseAbstractActivity
import com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography.EncriptionUtil
import com.crazylegend.kotlinextensions.log.debug


class MainAbstractActivity : BaseAbstractActivity() {


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
